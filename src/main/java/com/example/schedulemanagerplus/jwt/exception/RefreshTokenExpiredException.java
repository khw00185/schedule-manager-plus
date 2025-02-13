package com.example.schedulemanagerplus.jwt.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class RefreshTokenExpiredException extends CustomException {
    public RefreshTokenExpiredException() {
        super(CommonErrorCode.REFRESH_TOKEN_EXPIRED);
    }
}
