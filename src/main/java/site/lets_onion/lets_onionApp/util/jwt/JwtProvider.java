package site.lets_onion.lets_onionApp.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.exception.UncheckException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class JwtProvider {

    static private String issuer;
    static private Long accessExpiration;
    static private Long refreshExpiration;
    static private SecretKey secretKey;
    static private BlackList blackList;

    @Autowired
    public JwtProvider(@Value("${spring.application.name}") String issuer,
                       @Value("${service.jwt.access-expiration}") Long accessExpiration,
                       @Value("${service.jwt.refresh-expiration}") Long refreshExpiration,
                       @Value("${service.jwt.secret-key}") String secretKey,
                       BlackList blackList) {
        this.issuer = issuer;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.blackList = blackList;
    }


    /*액세스 토큰 발급*/
    public String createAccessToken(Long memberId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessExpiration);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(memberId.toString())
                .claim("type", TokenType.ACCESS)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }


    /*리프레시 토큰 발급*/
    public String createRefreshToken(Long memberId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(memberId.toString())
                .claim("type", TokenType.REFRESH)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }


    /*리프레시 토큰 검증 후 새로운 액세스 토큰 발급*/
    public Map<String, String> refreshAccessToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken);
            if (!claims.getPayload().get("type").equals(TokenType.REFRESH)
                    && !claims.getPayload().getIssuedAt().after(new Date())) {
                throw new UncheckException(Exceptions.INVALID_TOKEN);
            }
            Date expireAt = claims.getPayload().getExpiration();
            if (expireAt.before(new Date())) {
                throw new UncheckException(Exceptions.EXPIRED_TOKEN);
            }
            if (blackList.containsKey(refreshToken)) {
                throw new UncheckException(Exceptions.BLACKLISTED_TOKEN);
            }
            Long memberId = Long.parseLong(claims.getPayload().getSubject());
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("access", createAccessToken(memberId));
            tokenMap.put("refresh", createRefreshToken(memberId));
            blackList.put(refreshToken, expireAt);
            return tokenMap;
        } catch (Exception e) {
            throw new UncheckException(Exceptions.INTERNAL_SERVER_ERROR);
        }
    }


    /*토큰 유효성, 만료일자 확인*/
    public boolean validateToken(HttpServletRequest request) {
        String token = this.resolveToken(request);
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return !claims.getPayload().getExpiration().before(new Date())
                    && !claims.getPayload().getIssuedAt().after(new Date())
                    && claims.getPayload().get("type").equals(TokenType.ACCESS);
        } catch (Exception e) {
            throw new UncheckException(Exceptions.INVALID_TOKEN);
        }
    }


    /*요청 헤더에서 토큰 추출*/
    public String resolveToken(HttpServletRequest request) {
        try{
            String bearerToken = request.getHeader("Authorization");
            String token = bearerToken.replace("Bearer ", "");
            return token;
        } catch (Exception e) {
            throw new UncheckException(Exceptions.INVALID_TOKEN);
        }
    }


    /*요청에서 유저 ID 추출*/
    public Long getMemberId(HttpServletRequest request) {
        String token = resolveToken(request);
        String memberId = Jwts.parser().verifyWith(secretKey)
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
        return Long.parseLong(memberId);
    }
}
