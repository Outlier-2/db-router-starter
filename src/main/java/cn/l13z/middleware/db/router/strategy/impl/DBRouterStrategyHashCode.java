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

    private final DBRouterConfig dbRouterConfig;

    public DBRouterStrategyHashCode(DBRouterConfig dbRouterConfig) {
        this.dbRouterConfig = dbRouterConfig;
        logger.info("路由策略：哈希算法");
    }

    @Override
    public void doRouter(String dbKeyAttr) {

        logger.info("开始路由计算");
        int size = dbRouterConfig.getDbCount() * dbRouterConfig.getTbCount();

        // 扰动函数
        int idx = (size - 1) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode() >>> 16));

        // 库表索引
        int dbIdx = idx / dbRouterConfig.getTbCount() + 1;
        int tbIdx = idx - dbRouterConfig.getTbCount() * (dbIdx - 1);

        logger.info("路由计算结果：dbIdx：{} tbIdx：{}", dbIdx, tbIdx);

        // 设置到 ThreadLocal
        DBContextHolder.setDbKey(String.format("%02d", dbIdx));
        DBContextHolder.setTbKey(String.format("%03d", tbIdx));
        logger.info("查看是否把相关信息保存只DBContext中 DBKEY: {} TBKEY: {} ", DBContextHolder.getDbKey(),
            DBContextHolder.getTbKey());
    }

    @Override
    public void clear() {
        DBContextHolder.clearDbKey();
        DBContextHolder.clearTbKey();
    }
}
