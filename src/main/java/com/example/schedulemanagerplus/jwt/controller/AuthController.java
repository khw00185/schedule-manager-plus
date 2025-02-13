package com.example.schedulemanagerplus.jwt.controller;

import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.jwt.dto.TokenDto;
import com.example.schedulemanagerplus.jwt.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDto<?>> refresh(@CookieValue("Refresh-Token") String refreshToken, HttpServletResponse response){
        TokenDto tokenDto = authService.refresh(refreshToken);
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Set-Cookie", "Refresh-Token="+ tokenDto.getRefreshToken() + "; path=/; HttpOnly; SameSite=Strict");//Secure; 테스트를 위해 제거
        return ResponseEntity.ok(ResponseDto.success("재발급 완료"));
    }
}
