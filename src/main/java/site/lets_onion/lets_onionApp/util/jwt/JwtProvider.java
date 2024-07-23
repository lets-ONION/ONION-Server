package site.lets_onion.lets_onionApp.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.exception.CustomException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${spring.application.name}")
    private String issuer;
    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;
    @Value("${service.jwt.refresh-expiration}")
    private Long refreshExpiration;
    @Value("${service.jwt.secret-key}")
    private String key;
    private SecretKey secretKey;
    private final BlackList blackList;


    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }


    /*액세스 토큰 발급*/
    public String createAccessToken(Long memberId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessExpiration);

        return Jwts.builder()
                .header().type("JWT").and()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(memberId.toString())
                .claim("type", TokenType.ACCESS.name())
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
                .header().type("JWT").and()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(memberId.toString())
                .claim("type", TokenType.REFRESH.name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }


    /*리프레시 토큰 검증 후 새로운 액세스 토큰 발급*/
    public Map<String, String> refreshAccessToken(String refreshToken) {
        Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken);
        if (!claims.getPayload().get("type").equals(TokenType.REFRESH.name())) {
            throw new CustomException(Exceptions.INVALID_TOKEN);
        }
        if (claims.getPayload().getIssuedAt().after(new Date())) {
            throw new CustomException(Exceptions.INVALID_ISSUED_TIME);
        }
        Date expireAt = claims.getPayload().getExpiration();
        if (expireAt.before(new Date())) {
            throw new CustomException(Exceptions.EXPIRED_TOKEN);
        }
        if (blackList.containsKey(refreshToken)) {
            throw new CustomException(Exceptions.BLACKLISTED_TOKEN);
        }
        Long memberId = Long.parseLong(claims.getPayload().getSubject());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("access", createAccessToken(memberId));
        tokenMap.put("refresh", createRefreshToken(memberId));
        blackList.put(refreshToken, expireAt.toString());
        return tokenMap;
    }


    /*토큰 유효성, 만료일자 확인*/
    public boolean validateToken(HttpServletRequest request) {
        String token = this.resolveToken(request);
        Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        if (claims.getPayload().getExpiration().before(new Date())) {
            throw new CustomException(Exceptions.INVALID_TOKEN);
        } else if (!claims.getPayload().get("type").equals(TokenType.ACCESS.name())) {
            throw new CustomException(Exceptions.NOT_ACCESS_TOKEN);
        } else if (claims.getPayload().getIssuedAt().after(new Date())) {
            throw new CustomException(Exceptions.INVALID_ISSUED_TIME);
        }
        return true;
    }


    /*요청 헤더에서 토큰 추출*/
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = bearerToken.replace("Bearer ", "");
        return token;
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
