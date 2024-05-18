package cn.l13z.middleware.db.router;

import org.springframework.stereotype.Component;

/**
 * ClassName: DBRouterConfig.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 05:48 <br> Description: 路由配置 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 路由配置 <br>
 */
@Component
public class DBRouterConfig {

    /**
     * 分库的数量
     */
    private int dbCount;

    /**
     * 分表的数量
     */
    private int tbCount;

    /**
     * 路由字段
     */
    private String routerKey;

    public DBRouterConfig(int dbCount, int tbCount, String routerKey) {
        this.dbCount = dbCount;
        this.tbCount = tbCount;
        this.routerKey = routerKey;
    }
    public DBRouterConfig() {

    }

    public int getTbCount() {
        return tbCount;
    }

    public void setTbCount(int tbCount) {
        this.tbCount = tbCount;
    }

    public String getRouterKey() {
        return routerKey;
    }

    public void setRouterKey(String routerKey) {
        this.routerKey = routerKey;
    }

    public int getDbCount() {
        return dbCount;
    }

    public void setDbCount(int dbCount) {
        this.dbCount = dbCount;
    }
}
