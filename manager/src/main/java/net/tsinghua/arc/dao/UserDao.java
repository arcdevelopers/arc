package net.tsinghua.arc.dao;

import net.tsinghua.arc.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ji on 16-11-15.
 */
public interface UserDao {

    void addUser(User user);

    void updateImageUrl(User user);

    User queryByNameAndPwd(User user);

    void minusBalance(@Param("userId") Integer userId, @Param("money") int money);

    void increBalance(@Param("planId") Integer planId, @Param("money") double money);
}
