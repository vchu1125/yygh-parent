package com.atguigu.yygh.hosp.mongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.http.conn.util.DomainType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
;
import java.util.Date;
import java.util.List;


/**
 * @Author Weizhu
 * @Date 2023/8/4 20:50
 * @注释
 */
@SpringBootTest
public class MongoTemplateTest {
    @Resource
    private MongoTemplate mongoTemplate;

    //添加
    @Test
    public void addTest(){
        User user = new User();
        user.setName("奥特曼");
        user.setAge(30);
        user.setCreateDate(new Date());
        User insert = mongoTemplate.insert(user);
        System.out.println(insert);
    }
    //查询所有
    @Test
    public void findAllTest(){

        List<User> userList = mongoTemplate.findAll(User.class);
        userList.forEach(System.out::println);

    }
    //根据id查询
    @Test
    public void findByIdTest(){

        User user = mongoTemplate.findById("64ccf50d6b0eab587335b2d4", User.class);
        System.out.println(user);

    }
    //修改
    @Test
    public void updateTest(){
        //查询条件
        Criteria criteria = Criteria.where("_id").is("64ccf50d6b0eab587335b2d4");
        Query query = new Query(criteria);
        //更新内容
        Update update = new Update();
        update.set("name","钢铁侠");
        update.set("age",50);

        UpdateResult result = mongoTemplate.upsert(query, update, User.class);
        System.out.println("result = " + result);

    }
    //删除
    @Test
    public void removeTest(){
        Criteria criteria = Criteria.where("_id").is("64ccf50d6b0eab587335b2d4");
        Query query = new Query(criteria);
        DeleteResult deleteResult = mongoTemplate.remove(query, User.class);
        System.out.println("deleteResult = " + deleteResult);
    }
    //条件查询 and
    @Test
    public void queryFindTest(){
        Criteria criteria = Criteria.where("name").is("赵四").and("age").is(20);
        Query query = new Query(criteria);
        List<User> userList = mongoTemplate.find(query, User.class);
        userList.forEach(System.out::println);
    }
    //模糊查询
    @Test
    public void fuzzyQueryFindTest() {
        Criteria criteria = Criteria.where("name").is("赵四").and("age").is(20);
        Query query = new Query(criteria);
        List<User> userList = mongoTemplate.find(query, User.class);
        userList.forEach(System.out::println);

    }
    //分页查询
}
