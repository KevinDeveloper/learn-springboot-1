package com.kevin.springbootone.testRedis.service;

import com.kevin.springbootone.testRedis.JPA.RedisUserJPA;
import com.kevin.springbootone.testRedis.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Autowired
    private RedisUserJPA redisUserJPA;

    @Cacheable
    public List<UserEntity> list()
    {
        return redisUserJPA.findAll();
    }
}