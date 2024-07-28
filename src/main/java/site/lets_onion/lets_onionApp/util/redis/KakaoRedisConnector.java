package site.lets_onion.lets_onionApp.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class KakaoRedisConnector {

    private final RedisTemplate<String, KakaoTokens> redisTemplate;

    /*TTL 없이 카카오 토큰 저장*/
    public void set(Long memberId, KakaoTokens tokens){
        redisTemplate.opsForValue().set(memberId.toString(), tokens);
    }

    /*TTL과 함께 카카오 액세스 토큰 저장*/
    public void setWithTtl(Long memberId, KakaoTokens tokens, Long ttl){
        redisTemplate.opsForValue().set(memberId.toString(), tokens, ttl, TimeUnit.SECONDS);
    }


    /*유저 ID로 카카오 토큰 조회*/
    public KakaoTokens get(Long memberId){
        if (!exists(memberId)){
            throw new CustomException(Exceptions.KAKAO_TOKEN_NOT_FOUND);
        }
        return redisTemplate.opsForValue().get(memberId.toString());
    }


    /*카카오 토큰 삭제*/
    public void remove(Long memberId){
        redisTemplate.delete(memberId.toString());
    }


    /*카카오 토큰 존재 확인*/
    public boolean exists(Long memberId){
        return redisTemplate.opsForValue().get(memberId.toString()) != null;
    }
}
