package site.lets_onion.lets_onionApp.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

import javax.crypto.SecretKey;
import java.util.Date;
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
    public String createToken(Long memberId, TokenType tokenType) {
        Date expiration;
        Date now = new Date();
        if (tokenType == TokenType.ACCESS) {
            expiration = new Date(now.getTime() + accessExpiration);
        } else {
            expiration = new Date(now.getTime() + refreshExpiration);
        }

        return Jwts.builder()
                .header().type("JWT").and()
                .id(UUID.randomUUID().toString())
                .issuer(issuer)
                .subject(memberId.toString())
                .claim("type", tokenType.name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }


    /*리프레시 토큰 검증 후 새로운 액세스 토큰 발급*/
    public TokenDTO refreshAccessToken(String refreshToken) {
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
        if (blackList.containsToken(refreshToken)) {
            throw new CustomException(Exceptions.BLACKLISTED_TOKEN);
        }
        Long memberId = Long.parseLong(claims.getPayload().getSubject());
        blackList.putToken(refreshToken, expireAt.toString());
        return new TokenDTO(createToken(memberId, TokenType.REFRESH),
                createToken(memberId, TokenType.REFRESH));
    }


    /*로그아웃*/
    public void logout(String accessToken, String refreshToken) {
        Date accessTokenExpiration = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(accessToken).getPayload().getExpiration();
        Date refreshTokenExpiration = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(refreshToken).getPayload().getExpiration();

        blackList.putToken(accessToken, accessTokenExpiration.toString());
        blackList.putToken(refreshToken, refreshTokenExpiration.toString());
    }


    /*토큰 유효성, 만료일자 확인*/
    public String validateToken(HttpServletRequest request) {
        String token = this.resolveToken(request);
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        Date now = new Date();

        if (blackList.containsToken(token)) {
            throw new CustomException(Exceptions.BLACKLISTED_TOKEN);
        } else if (claims.getExpiration().before(now)) {
            throw new CustomException(Exceptions.EXPIRED_TOKEN);
        } else if (claims.getIssuedAt().after(new Date())) {
            throw new CustomException(Exceptions.INVALID_ISSUED_TIME);
        } else if (!claims.get("type").equals(TokenType.ACCESS.name())) {
            throw new CustomException(Exceptions.NOT_ACCESS_TOKEN);
        } return token;
    }


    /*요청 헤더에서 토큰 추출*/
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return bearerToken.replace("Bearer ", "");
    }


    /*요청에서 유저 ID 추출*/
    public Long getMemberId(HttpServletRequest request) {
        String token = validateToken(request);
        String memberId = Jwts.parser().verifyWith(secretKey)
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
        return Long.parseLong(memberId);
    }
}
