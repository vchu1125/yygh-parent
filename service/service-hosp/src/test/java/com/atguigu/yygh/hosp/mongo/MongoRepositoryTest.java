package com.atguigu.yygh.hosp.mongo;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author Weizhu
 * @Date 2023/8/4 18:51
 * @注释
 */
@SpringBootTest
public class MongoRepositoryTest {
    @Resource
    private UserRepository userRepository;

    //插入
    @Test
    public void insertTest() {
        User user = new User();
        user.setName("刘能");
        user.setAge(10);
        user.setCreateDate(new Date());
        userRepository.insert(user);
    }

    //查询所有
    @Test
    public void findAllTest() {
        List<User> userList = userRepository.findAll();
        userList.forEach(System.out::println);
    }

    //根据id查询
    @Test
    public void getByIdTest() {
        Optional<User> present = userRepository.findById("64ccdc460128b1094c1d63bf");
        boolean present1 = present.isPresent();
        if (present1) {
            User user = present.get();
            System.out.println(user);
        }
    }

    //条件查询
    @Test
    public void ConditionQueryTest() {
        User user = new User();
        user.setAge(20);
        Example<User> example = Example.of(user);
        List<User> userList = userRepository.findAll(example);
        userList.forEach(System.out::println);
    }

        //排序查询
        @Test
        public void SortQueryTest() {
            Sort sort = Sort.by(Sort.Order.asc("age"));
            List<User> userList = userRepository.findAll(sort);
            userList.forEach(System.out::println);
        }
        //分页查询
        @Test
        public void findPageTest() {

            PageRequest pageRequest = PageRequest.of(1, 2);
            Page<User> userPage = userRepository.findAll(pageRequest);
            userPage.forEach(System.out::println);
        }
        //更新
        @Test
        public void updateTest() {
            //先查询
            Optional<User> present = userRepository.findById("64ccdc460128b1094c1d63bf");
            boolean presentPresent = present.isPresent();
            if (presentPresent) {
                User user = present.get();
                user.setAge(40);
                User save = userRepository.save(user);
                System.out.println(save);
            }

        }
    //删除
    @Test
    public void removeTest(){
        userRepository.deleteById("64ccdc460128b1094c1d63bf");
        System.out.println("删除***");

    }
    //根据年龄查询
    @Test
    public void findByAge(){
//        List<User> byAge = userRepository.findByAge(20);
//        byAge.forEach(System.out::println);
        List<User> userList = userRepository.findByNameAndAge("赵四", 20);
        userList.forEach(System.out::println);

    }

}
