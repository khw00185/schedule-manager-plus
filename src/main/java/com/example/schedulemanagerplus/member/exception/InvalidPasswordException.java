package com.example.schedulemanagerplus.member.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class InvalidPasswordException extends CustomException {
    public InvalidPasswordException() {
        super(CommonErrorCode.InvalidPassword);
    }
}
