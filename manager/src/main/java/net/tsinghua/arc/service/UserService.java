package net.tsinghua.arc.service;

import net.tsinghua.arc.dao.UserDao;
import net.tsinghua.arc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ji on 16-11-15.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void addUser(User user) throws Exception{
        userDao.addUser(user);
    }

    public void updateImageUrl(User user) throws Exception{
        userDao.updateImageUrl(user);
    }

    public User queryByNameAndPwd(User user) throws Exception{
        return userDao.queryByNameAndPwd(user);
    }

    public User queryById(Integer userId) throws Exception{
        return userDao.queryById(userId);
    }
}
