package com.turing.test.service.util;

import com.google.common.base.Strings;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
* @Author Yibo Wen
* @Date 11/11/2021 11:29 PM
**/
@Slf4j
@Service
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Boolean tryLock(String lockKey, String clientId, long millis){
        if(Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, clientId, Duration.ofMillis(millis)))){
            return true;
        }
        log.warn("RedisLock->tryLock: lock error");
        return false;
    }

    public Boolean tryUnlock(String lockKey, String clientId){
        String id = redisTemplate.opsForValue().get(lockKey);
        if(!Strings.isNullOrEmpty(id) && id.equals(clientId)){
            return redisTemplate.opsForValue().getOperations().delete(lockKey);
        }
        log.warn("RedisLock->tryUnlock: unlock error");
        throw new RedisException("Unlock failed");
    }
}
