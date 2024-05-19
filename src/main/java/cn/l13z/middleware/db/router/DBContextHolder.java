package cn.l13z.middleware.db.router;

/**
 * ClassName: DBContextHolder.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 05:38 <br> Description: 数据源的上下文 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 数据源的上下文 <br>
 */
public class DBContextHolder {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DBContextHolder.class);

    private static final ThreadLocal<String> DB_KEY = new ThreadLocal<>();
    private static final ThreadLocal<String> TB_KEY = new ThreadLocal<>();

    public static String getDbKey() {
        return DB_KEY.get();
    }

    public static void setDbKey(String dbKeyIdx) {
        DB_KEY.set(dbKeyIdx);
    }

    public static String getTbKey() {
        return TB_KEY.get();
    }

    public static void setTbKey(String tbKeyIdx) {
        TB_KEY.set(tbKeyIdx);
    }

    public static void clearDbKey() {
        DB_KEY.remove();
    }

    public static void clearTbKey() {
        TB_KEY.remove();
    }

}
