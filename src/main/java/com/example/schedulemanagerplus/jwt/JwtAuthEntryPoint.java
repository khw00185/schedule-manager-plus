package com.example.schedulemanagerplus.jwt;

import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.common.exception.dto.ErrorReason;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());

        ErrorReason errorReason = CommonErrorCode.InternalServerError.getErrorReason();

        response.setStatus(errorReason.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseDto<?> errorResponse = ResponseDto.fail(errorReason);
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);

    }

}
