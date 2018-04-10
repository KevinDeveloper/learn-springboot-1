package com.kevin.springbootone.JPA;

import com.kevin.springbootone.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public interface UserJPA extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity>,
        Serializable {
    //查询大于20岁的用户
    @Query(value = "select * from user where age > ?1",nativeQuery = true)
    public List<UserEntity> nativeQuery(int age);

    //根据用户名、密码删除一条数据
    @Transactional
    @Modifying
    @Query(value = "delete from user where name = ?1 and pwd = ?2",nativeQuery = true)
    public void deleteQuery(String name,String pwd);

}
