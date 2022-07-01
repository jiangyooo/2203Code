package com.qf.config;

import com.qf.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JiangBing
 * @date 2022/6/30 16:47
 */
@Configuration
public class UserConfig {

    @Bean(name = "user1")
    public User user(){
        User user = new User();
        user.setId(1);
        user.setName("张三");
        return user;
    }

}
