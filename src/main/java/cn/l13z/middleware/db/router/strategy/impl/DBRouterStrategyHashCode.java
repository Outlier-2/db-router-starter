package cn.l13z.middleware.db.router.strategy.impl;

import cn.l13z.middleware.db.router.DBContextHolder;
import cn.l13z.middleware.db.router.DBRouterConfig;
import cn.l13z.middleware.db.router.strategy.IDBRouterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: DBRouterStrategyHashCode.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 07:41 <br> Description: 路由策略哈希算法实现类 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 路由策略哈希算法实现类 <br>
 */
public class DBRouterStrategyHashCode implements IDBRouterStrategy {

    private final Logger logger = LoggerFactory.getLogger(DBRouterStrategyHashCode.class);

    private DBRouterConfig dbRouterConfig;
    @Override
    public void doRouter(String dbKeyAttr) {
        int size = dbRouterConfig.getDbCount() * dbRouterConfig.getTbCount();

        // 扰动函数
        int idx = (size - 1) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode() >>> 16));

        // 库表索引
        int dbIdx = idx / dbRouterConfig.getTbCount() + 1;
        int tbIdx = idx - dbRouterConfig.getTbCount() * (dbIdx - 1);

        // 设置到 ThreadLocal
        DBContextHolder.setDbKey(String.format("%02d", dbIdx));
        DBContextHolder.setTbKey(String.format("%03d", tbIdx));
        logger.debug("数据库路由 dbIdx：{} tbIdx：{}",  dbIdx, tbIdx);
    }

    @Override
    public void clear() {
        DBContextHolder.clearDbKey();
        DBContextHolder.clearTbKey();
    }
}
