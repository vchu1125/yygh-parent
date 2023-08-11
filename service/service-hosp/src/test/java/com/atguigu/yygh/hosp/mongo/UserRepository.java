package com.atguigu.yygh.hosp.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author Weizhu
 * @Date 2023/8/4 18:44
 * @注释
 *
 * Type parameters:
 * <T> – the domain type the repository manages存储库管理的域类型
 * <ID> – the type of the id of the entity the repository manages存储库管理的实体id的类型
 */
public interface UserRepository extends MongoRepository<User,String> {
    List<User> findByAge(Integer age);
    List<User> findByNameAndAge(String name,Integer age);
}
