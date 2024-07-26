package site.lets_onion.lets_onionApp.util.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import site.lets_onion.lets_onionApp.util.redis.ServiceRedisConnector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component @Primary
@RequiredArgsConstructor
public class RedisBlackList implements BlackList {

    private final ServiceRedisConnector serviceRedisConnector;
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
        serviceRedisConnector.setWithTtl(token, date, ttl);
    }

    @Override
    public boolean containsToken(String token) {
        return serviceRedisConnector.exists(token);
    }
}
