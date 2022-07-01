package com.qf.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author JiangBing
 * @date 2022/6/30 16:40
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String password;
    private String sex;
    private Date birthday;
    private Date registTime;
}
