package cn.l13z.middleware.db.router.config;

import cn.l13z.middleware.db.router.DBRouterConfig;
import cn.l13z.middleware.db.router.DBRouterJoinPoint;
import cn.l13z.middleware.db.router.dynamic.DynamicDataSource;
import cn.l13z.middleware.db.router.dynamic.DynamicMybatisPlugin;
import cn.l13z.middleware.db.router.util.PropertyUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;
import org.aopalliance.intercept.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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

    @Bean(name = "db-router-point")
    @ConditionalOnMissingBean
    public DBRouterJoinPoint point() {
        return new DBRouterJoinPoint();
    }

    @Bean
    public DBRouterConfig dbRouterConfig() {
        return new DBRouterConfig(dbCount, tbCount, routerKey);
    }

    @Bean
    public Interceptor plugin() {
        return (Interceptor) new DynamicMybatisPlugin();
    }

    @Bean
    public DataSource dataSource() {
        // 创建数据源
        Map<Object, Object> targetDataSources = new HashMap<>();
        for (String dbInfo : dataSourceMap.keySet()) {
            Map<String, Object> objMap = dataSourceMap.get(dbInfo);
            targetDataSources.put(dbInfo, new DriverManagerDataSource(objMap.get("url").toString(), objMap.get("username").toString(), objMap.get("password").toString()));
        }

        // 设置数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(new DriverManagerDataSource(defaultDataSourceConfig.get("url").toString(), defaultDataSourceConfig.get("username").toString(), defaultDataSourceConfig.get("password").toString()));

        return dynamicDataSource;
    }
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
