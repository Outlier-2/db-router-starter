package cn.l13z.middleware.db.router.dynamic;

import cn.l13z.middleware.db.router.DBContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * ClassName: DynamicDataSource.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 07:06 <br> Description: 动态数据源 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 动态数据源 <br>
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return "db" + DBContextHolder.getDbKey();
    }
}
