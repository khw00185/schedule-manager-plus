package com.example.schedulemanagerplus.jwt.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class NotFoundTokenException extends CustomException {
    public NotFoundTokenException() {
        super(CommonErrorCode.NotFoundToken);
    }
}
