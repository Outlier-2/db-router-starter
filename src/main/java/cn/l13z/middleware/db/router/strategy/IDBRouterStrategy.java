package cn.l13z.middleware.db.router.strategy;

/**
 * ClassName: IDBRouterStrategy.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 07:40 <br> Description: 路由策略 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 路由策略 <br>
 */
public interface IDBRouterStrategy {

    void doRouter(String dbKeyAttr);
    void clear();
}
