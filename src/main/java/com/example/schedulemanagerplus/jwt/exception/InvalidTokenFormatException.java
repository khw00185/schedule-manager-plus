package com.example.schedulemanagerplus.jwt.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class InvalidTokenFormatException extends CustomException {
    public InvalidTokenFormatException() {
        super(CommonErrorCode.InvalidTokenFormat);
    }
}
