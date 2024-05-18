package cn.l13z.middleware.db.router;

/**
 * ClassName: DBRouterBase.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 05:37 <br> Description: 数据库基础信息 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 数据库基础信息 <br>
 */
public class DBRouterBase {
    private String tbIdx;

    public String getTbIdx() {
        return DBContextHolder.getTbKey();
    }
}
