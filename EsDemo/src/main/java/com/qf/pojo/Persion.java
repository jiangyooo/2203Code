package com.qf.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author JiangBing
 * @date 2022/7/1 11:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persion {

    //json转换的时候忽略这个id
    @JsonIgnore
    private String id;

    //姓名
    private String name;

    //年龄
    private Integer age;

    //出生日期, 日期格式必须和ES中这个索引字段的日期格式保持一致
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


}
