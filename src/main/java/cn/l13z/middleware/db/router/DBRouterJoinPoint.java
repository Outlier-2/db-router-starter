package cn.l13z.middleware.db.router;

import cn.l13z.middleware.db.router.annotation.DBRouter;
import cn.l13z.middleware.db.router.strategy.IDBRouterStrategy;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: DBRouterJoinPoint.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 05:53 <br> Description: 数据库连接点 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 数据库连接点 <br>
 */
@Aspect
public class DBRouterJoinPoint {

    private final Logger logger = LoggerFactory.getLogger("DBRouterJoinPoint");

    private final DBRouterConfig dbRouterConfig;

    private final IDBRouterStrategy dbRouterStrategy;

    public DBRouterJoinPoint(DBRouterConfig dbRouterConfig, IDBRouterStrategy dbRouterStrategy) {
        this.dbRouterConfig = dbRouterConfig;
        this.dbRouterStrategy = dbRouterStrategy;

    }

    @Pointcut("@annotation(cn.l13z.middleware.db.router.annotation.DBRouter)")
    public void aopPoint() {

    }

    @Around("aopPoint() && @annotation(dbRouter)")
    public Object doRouter(ProceedingJoinPoint jp, DBRouter dbRouter) throws Throwable {
        String dbKey = dbRouter.key();
        if (StringUtils.isBlank(dbKey) && StringUtils.isBlank(dbRouterConfig.getRouterKey())) {
            throw new RuntimeException("annotation DBRouter key is null");
        }

        dbKey = StringUtils.isNotBlank(dbKey) ? dbKey : dbRouterConfig.getRouterKey();
        logger.info("路由属性初次获取：{}", dbKey);
        // 路由属性
        String dbKeyAttr = getAttrValue(dbKey, jp.getArgs());
        //路由策略
        dbRouterStrategy.doRouter(dbKeyAttr);

        logger.info("路由属性：{}，路由策略：{}", dbKeyAttr, dbRouterStrategy.getClass().getSimpleName());
        //返回结果
        // 返回结果
        try {
            return jp.proceed();
        } finally {
            dbRouterStrategy.clear();
        }
    }

    public String getAttrValue(String attr, Object[] args) {
        if (1 == args.length) {
            return args[0].toString();
        }

        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                filedValue = BeanUtils.getProperty(arg, attr);
            } catch (Exception e) {
                logger.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return filedValue;
    }

}
