package com.turing.test.service.util;

import io.lettuce.core.RedisException;
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

    // Add to set with key
    public Boolean addToSet(String key, String id){
        while(true){
            if(redisLock.tryLock(key,id,500L)){
                Boolean result = redisTemplate.opsForSet().add(key, id) == 1;
                redisLock.tryUnlock(key,id);
                return result;
            }
            log.warn("RedisUtils->addToSet: failed to get lock, retrying...");
        }
    }

    // Add to Hash with key
    public Boolean addToHash(String key, String id, String target){
        while(true){
            if(redisLock.tryLock(key,id,500L)){
                Boolean result = redisTemplate.opsForHash().putIfAbsent(key,id,target);
                redisLock.tryUnlock(key,id);
                return result;
            }
            log.warn("RedisUtils->addToHash: failed to get lock, retrying...");
        }
    }

    // Find two values under given key, return a random value other than itself and remove both
    // If failed, return null
    public String popTwo(String key, String id){
        while(true){
            if(redisLock.tryLock(key,id,500L)){
                if(redisTemplate.hasKey(key) && redisTemplate.opsForSet().size(key)>=2){
                    String result = redisTemplate.opsForSet().remove(key,id) == 1 ? redisTemplate.opsForSet().pop(key) : null;
                    redisLock.tryUnlock(key,id);
                    return result;
                }
                return null;
            }
            log.warn("RedisUtils->popTwo: failed to get lock, retrying...");
        }
    }

    public boolean findInSet(String key, String id){
        while(true){
            if(redisLock.tryLock(key,id,500L)){
                Boolean result = redisTemplate.opsForSet().isMember(key,id);
                redisLock.tryUnlock(key,id);
                return result;
            }
            log.warn("RedisUtils->addToHash: failed to get lock, retrying...");
        }
    }

    public String getValueInHash(String key, String id){
        while(true){
            if(redisLock.tryLock(key,id,500L)){
                String result = redisTemplate.opsForHash().get(key,id).toString();
                redisLock.tryUnlock(key,id);
                return result;
            }
            log.warn("RedisUtils->getValueInHash: failed to get lock, retrying...");
        }
    }

}