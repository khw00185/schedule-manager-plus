package com.example.schedulemanagerplus.jwt;

import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;
import com.example.schedulemanagerplus.jwt.entity.AuthMember;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtSecurityFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (PATH_MATCHER.match("/api/members/signin", requestURI) ||
                PATH_MATCHER.match("/api/members/signup", requestURI) ||
                PATH_MATCHER.match("/api/auth/refresh", requestURI) ||
                ("GET".equals(method) && PATH_MATCHER.match("/api/schedules", requestURI)) ||
                ("GET".equals(method) && PATH_MATCHER.match("/api/comments", requestURI))
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String jwt = jwtUtil.substringToken(authorization);
            try {
                Claims claims = jwtUtil.extractClaims(jwt);

                String userId = claims.getSubject();
                String userName = claims.get("name", String.class);
                if(SecurityContextHolder.getContext().getAuthentication() == null){
                    AuthMember authMember = new AuthMember(userId, userName);

                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authMember);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (ExpiredJwtException e) {
                log.warn("🔴 JWT 토큰이 만료되었습니다: {}", e.getMessage());
                sendErrorResponse(response, CommonErrorCode.ACCESS_TOKEN_EXPIRED);
                return;
            } catch (MalformedJwtException | SecurityException | UnsupportedJwtException | IllegalArgumentException e){
                log.error("Invalid JWT ", e);
                sendErrorResponse(response, CommonErrorCode.InvalidTokenFormat);
                return;
            } catch (Exception e) {
                log.error("Internal server error", e);
                sendErrorResponse(response, CommonErrorCode.InternalServerError);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, CommonErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getErrorReason().getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseDto<?> errorResponse = ResponseDto.fail(errorCode.getErrorReason());
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }

}
