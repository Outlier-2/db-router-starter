package cn.l13z.middleware.test;

import cn.l13z.middleware.db.router.annotation.DBRouter;
import java.lang.reflect.Method;
import org.junit.Test;

/**
 * ClassName: ApiInterceptorTest.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * Created: 2024-05-18 07:23 <br> Description: 拦截器测试 <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 拦截器测试 <br>
 */
public class ApiInterceptorTest {

    public static void main(String[] args) {
        System.out.println("Hi");
    }

    @Test
    public void test_db_hash() {
        String key = "Ukdli109op89oi";

        int dbCount = 2, tbCount = 4;
        int size = dbCount * tbCount;
        // 散列
        int idx = (size - 1) & (key.hashCode() ^ (key.hashCode() >>> 16));

        int dbIdx = idx / tbCount + 1;
        int tbIdx = idx - tbCount * (dbIdx - 1);

        System.out.println(dbIdx);
        System.out.println(tbIdx);

    }

    @Test
    public void test_str_format() {
        System.out.println(String.format("db%02d", 1));
        System.out.println(String.format("_%02d", 25));
    }

    @Test
    public void test_annotation() throws NoSuchMethodException {
        Class<IUserDao> iUserDaoClass = IUserDao.class;
        Method method = iUserDaoClass.getMethod("insertUser", String.class);

        DBRouter dbRouter = method.getAnnotation(DBRouter.class);

        System.out.println(dbRouter.key());

    }


}
