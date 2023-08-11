package com.atguigu.yygh.hosp.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @Author Weizhu
 * @Date 2023/8/4 18:40
 * @注释
 */
@Data
@Document("user")//指定mongodb中的集合名字
public class User {
    @Id
    private String id;
    private String name;
    private Integer age;
    private Date createDate;
}
