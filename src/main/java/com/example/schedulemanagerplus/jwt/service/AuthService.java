package com.example.schedulemanagerplus.jwt.service;

import com.example.schedulemanagerplus.jwt.JwtUtil;
import com.example.schedulemanagerplus.jwt.dto.TokenDto;
import com.example.schedulemanagerplus.jwt.entity.RefreshToken;
import com.example.schedulemanagerplus.jwt.exception.NotFoundTokenException;
import com.example.schedulemanagerplus.jwt.exception.RefreshTokenExpiredException;
import com.example.schedulemanagerplus.jwt.exception.RefreshTokenMismatchException;
import com.example.schedulemanagerplus.jwt.repository.RefreshTokenRepository;
import com.example.schedulemanagerplus.member.entity.Member;
import com.example.schedulemanagerplus.member.exception.UserNotFoundException;
import com.example.schedulemanagerplus.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public TokenDto refresh(String token) {
        Claims claims = jwtUtil.extractClaims(token);

        if (claims.getExpiration().before(new Date())) {
            throw new RefreshTokenExpiredException();
        }

        Member member = memberRepository.findById(claims.getSubject())
                .orElseThrow(UserNotFoundException::new);

        RefreshToken storedRefreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .orElseThrow(NotFoundTokenException::new);

        if(!storedRefreshToken.getRefreshToken().equals(token)) {
            log.error("비정상적인 접근 감지");
            refreshTokenRepository.delete(storedRefreshToken);
            throw new RefreshTokenMismatchException();
        }

        refreshTokenRepository.delete(storedRefreshToken);

        TokenDto newTokens = jwtUtil.createToken(member.getId());

        //새로운 Refresh Token db에 저장
        RefreshToken newRefreshToken = new RefreshToken(newTokens.getRefreshToken(), member);
        refreshTokenRepository.save(newRefreshToken);

        return newTokens;
    }
}
