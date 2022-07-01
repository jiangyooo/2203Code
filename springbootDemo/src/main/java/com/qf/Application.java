package com.qf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author JiangBing
 * @date 2022/6/30 16:04
 */
@SpringBootApplication
@MapperScan("com.qf.dao")
public class Application {
    public static void main(String[] args) {
        System.out.println("123456");
        SpringApplication.run(Application.class,args);
    }
}
