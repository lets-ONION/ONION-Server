package site.lets_onion.lets_onionApp.util.jwt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MemoryBlackList implements BlackList {

    public ConcurrentHashMap<String, Date> blackList;
    public ScheduledExecutorService scheduler;
    private SimpleDateFormat formatter = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    /*어플리케이션이 실행될 때마다 블랙리스트 및 삭제 스케쥴러 재시작*/
    @PostConstruct
    public void init() {
        blackList = new ConcurrentHashMap<>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::removeExpiredTokens, 0, 1, TimeUnit.HOURS);
    }


    @Override
    public void putToken(String token, String date) {
        try {
            blackList.put(token, formatter.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean containsToken(String token) {
        return blackList.containsKey(token);
    }


    /*블랙리스트 내 토큰 자동 삭제*/
    private void removeExpiredTokens() {
        Date now = new Date();
        blackList.entrySet().removeIf(entry -> entry.getValue().before(now));
    }

    /*어플리케이션이 종료되면 블랙리스트 자동 삭제 스케쥴러 종료*/
    @PreDestroy
    public void cleanup() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
