package site.lets_onion.lets_onionApp.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component @Primary
@RequiredArgsConstructor
public class RedisBlackList implements BlackList {

    private final RedisTemplate<String, String> redisBlackList;
    private SimpleDateFormat formatter =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    @Override
    public void putToken(String token, String date) {
        Date parsedDate;
        try {
            parsedDate = formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long ttl = parsedDate.getTime() - new Date(System.currentTimeMillis()).getTime();
        redisBlackList.opsForValue().set(token, date, ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean containsToken(String token) {
        for (int i = 0; i < 100; i++) {
            System.out.println("레디스?");
        }
        if (redisBlackList.opsForValue().get(token) == null) {
            return false;
        }
        return true;
    }
}
