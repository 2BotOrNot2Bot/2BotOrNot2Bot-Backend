package com.turing.test.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
* @Author Yibo Wen
* @Date 11/18/2021 10:54 PM
**/
@Service
@Slf4j
public class RedisUtils {

    @Autowired
    RedisLock redisLock;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public Boolean addToSet(String key, String id){
        if(redisLock.tryLock(key,id,500L)){
            return redisTemplate.opsForSet().add(key, id) == 1;
        }
        return false;
    }

    public Boolean addToHash(String key, String id, String target){
        if(redisLock.tryLock(key,id,500L)){
            return redisTemplate.opsForHash().putIfAbsent(key,id,target);
        }
        return false;
    }

    // Find two values under given key, return a random value other and remove both
    // If failed, return null
    public String popTwo(String key, String id){
        if(redisLock.tryLock(key,id,500L)){
            if(redisTemplate.hasKey(key) && redisTemplate.opsForSet().size(key)>=2){
                return redisTemplate.opsForSet().remove(key,id) == 1 ? redisTemplate.opsForSet().pop(key) : null;
            }
        }
        return null;
    }

}
