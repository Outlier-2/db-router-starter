package cn.l13z.middleware.db.router.config;

import cn.l13z.middleware.db.router.util.PropertyUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * ClassName: DataSourceAutoConfig.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 06:27 <br> Description: 数据库源配置 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 数据库源配置 <br>
 */
@Configuration
public class DataSourceAutoConfig implements EnvironmentAware {

    /**
     * 数据库配置组
     */
    private final Map<String, Map<String, Object>> dataSourceMap = new HashMap<>();

    /**
     * 默认数据库配置
     */
    private Map<String, Object> defaultDataSourceConfig;
    /**
     * 数据库数量
     */
    private int dbCount;
    /**
     * 表数量
     */
    private int tbCount;
    /**
     * 路由字段
     */
    private String routerKey;


    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "mini-db-router.jdbc.datasource.";

        dbCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty(prefix + "dbCount")));
        tbCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty(prefix + "tbCount")));
        routerKey = environment.getProperty(prefix + "routerKey");

        // 分库分表数据源配置
        String  dataSource = environment.getProperty(prefix + "list");
        // TODO 暂时使用一个简单的判断是否数据源为空，后续需要完善
        assert dataSource != null;
        for (String dbInfo : dataSource.split(",")){
            Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + dbInfo, Map.class);
            dataSourceMap.putIfAbsent(dbInfo, dataSourceProps);
        }

        // 默认数据源配置
        String defaultDataSource = environment.getProperty(prefix + "default");
        defaultDataSourceConfig =PropertyUtil.handle(environment, prefix + defaultDataSource, Map.class);




    }
}
