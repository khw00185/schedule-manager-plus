package com.example.schedulemanagerplus.jwt;

import com.example.schedulemanagerplus.jwt.dto.TokenDto;
import com.example.schedulemanagerplus.jwt.exception.InvalidTokenFormatException;
import com.example.schedulemanagerplus.jwt.exception.NotFoundTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 15 * 60 * 1000L;  // 15분
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;  // 7일
    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public TokenDto createToken(String memberId) {
        Date date = new Date();
        String accessToken = Jwts.builder()
                .subject(memberId)
                .issuedAt(new Date())
                .expiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(memberId)
                .issuedAt(new Date())
                .expiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .signWith(secretKey)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("토큰을 찾을 수 없습니다.: " + tokenValue);
        throw new NotFoundTokenException();
    }

    public Claims extractClaims(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e){
            throw e;
        } catch (JwtException e){
            log.error("JWT 파싱 중 오류 발생", e);
            throw new InvalidTokenFormatException();
        }
    }




}
