package com.kevin.springbootone.testRedis.JPA;

import com.kevin.springbootone.testRedis.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedisUserJPA extends JpaRepository<UserEntity, Long>{
}
