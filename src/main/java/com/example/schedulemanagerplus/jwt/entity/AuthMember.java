package com.example.schedulemanagerplus.jwt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class AuthMember implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String userId;
    private final String userName;
}
