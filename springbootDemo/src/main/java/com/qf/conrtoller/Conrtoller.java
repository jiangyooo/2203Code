package com.qf.conrtoller;

import com.qf.dao.UserDao;

import com.qf.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author JiangBing
 * @date 2022/6/30 16:35
 */
@RestController
@RequestMapping("/test")
public class Conrtoller {

    @Autowired
    private UserDao userDao;


    @Resource(name = "user1")
    private User user;


    @RequestMapping("/hello")
    public String test() {
        System.out.println(user);
        return "Hello Spring";
    }


    @RequestMapping("select")
    @ResponseBody
    public List<User> select(){
//        List<User> userList = userDao.findAll();
        return userDao.findAll(1,10);
    }


    @RequestMapping("delete")
    @ResponseBody
    public Integer delete(@RequestBody User user) {
        return userDao.delete(user.getId());
    }

    @RequestMapping("update")
    @ResponseBody
    public Integer update(@RequestBody User user){
        return userDao.Update(user);
    }

    @RequestMapping("insert")
    @ResponseBody
    public Integer inset(@RequestBody User user){
        return userDao.insert(user);
    }
}
