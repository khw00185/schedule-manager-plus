package com.example.schedulemanagerplus.jwt;

import com.example.schedulemanagerplus.jwt.entity.AuthMember;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthMember authMember;

    public JwtAuthenticationToken(AuthMember authMember) {
        super(null);
        this.authMember = authMember;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authMember;
    }
}