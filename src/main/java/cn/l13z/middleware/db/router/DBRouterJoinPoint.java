package cn.l13z.middleware.db.router;

import cn.l13z.middleware.db.router.annotation.DBRouter;
import java.lang.reflect.Method;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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

    //    @Autowired
    private DBRouterConfig dbRouterConfig;

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

        // 计算路由
        String dbKeyAttr = getAttrValue(dbKey, jp.getArgs());
        int size = dbRouterConfig.getDbCount() * dbRouterConfig.getTbCount();

        // 扰动函数
        int idx = (size - 1) & (dbKeyAttr.hashCode() ^ (dbKeyAttr.hashCode() >>> 16));

        // 库表索引
        int dbIdx = idx / dbRouterConfig.getTbCount() + 1;
        int tbIdx = idx - dbRouterConfig.getTbCount() * (dbIdx - 1);

        // 设置到 ThreadLocal
        DBContextHolder.setDbKey(String.format("%02d", dbIdx));
        DBContextHolder.setTbKey(String.format("%03d", tbIdx));
        logger.info("数据库路由 method：{} dbIdx：{} tbIdx：{}", getMethod(jp).getName(), dbIdx, tbIdx);

        // 返回结果
        try {
            return jp.proceed();
        } finally {
            DBContextHolder.clearDbKey();
            DBContextHolder.clearTbKey();
        }
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
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
