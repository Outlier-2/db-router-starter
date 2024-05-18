package cn.l13z.middleware.test;

import cn.l13z.middleware.db.router.annotation.DBRouter;

/**
 * ClassName:     IUserDao.java <br>
 *
 * @author AlfredOrlando <br>
 * <p>
 * <p>
 * Created:  2024-05-18 07:24 <br> Description: 用户Dao <br>
 * <p>
 * Modification History: <br> - 2024/5/18 AlfredOrlando 用户Dao <br>
 */
public interface IUserDao {

    @DBRouter(key = "userId")
    void insertUser(String req);

}

