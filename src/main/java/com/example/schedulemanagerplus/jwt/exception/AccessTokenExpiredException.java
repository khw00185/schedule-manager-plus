package com.example.schedulemanagerplus.jwt.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class AccessTokenExpiredException extends CustomException {
    public AccessTokenExpiredException() {
        super(CommonErrorCode.ACCESS_TOKEN_EXPIRED);
    }
}
