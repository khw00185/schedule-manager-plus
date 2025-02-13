package com.example.schedulemanagerplus.jwt.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class RefreshTokenMismatchException extends CustomException {
    public RefreshTokenMismatchException() {
        super(CommonErrorCode.RefreshTokenMismatch);
    }
}
