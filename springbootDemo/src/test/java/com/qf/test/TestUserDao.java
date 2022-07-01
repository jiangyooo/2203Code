package com.qf.test;

import com.qf.dao.UserDao;
import com.qf.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author JiangBing
 * @date 2022/6/30 17:21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserDao {

//    @Autowired
//    private UserDao userDao;
//
//    @Test
//    public void testfindAll() {
//        List<User> userList = userDao.findAll(1,10);
//
//        System.out.println(userList);
//    }
//
//    @Test
//    public void insertUser() {
//       userDao.insert("江江","745641","男",new Date(),new Date());
//    }
//
//    @Test
//    public void deleteUser() {
//        Integer delete = userDao.delete(11);
//    }
//
//    @Test
//    public void updateUser() {
//        userDao.update("江江江",12);
//    }
}
