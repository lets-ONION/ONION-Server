package site.lets_onion.lets_onionApp.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ServiceRedisConnector {

    private final RedisTemplate<String, String> redisTemplate;


    /*TTL 없이 데이터 저장*/
    public void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    /*TTL과 함께 데이터 저장*/
    public void setWithTtl(String key, String value, Long ttl){
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
    }


    /*키로 값 조회*/
    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }


    /*데이터 삭제*/
    public void remove(String key){
        redisTemplate.delete(key);
    }


    /*데이터 존재 여부 확인*/
    public boolean exists(String key){
        return redisTemplate.opsForValue().get(key) != null;
    }
}
