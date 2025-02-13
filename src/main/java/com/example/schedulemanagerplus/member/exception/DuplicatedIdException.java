package com.example.schedulemanagerplus.member.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class DuplicatedIdException extends CustomException {
    public DuplicatedIdException() {
        super(CommonErrorCode.DuplicatedId);
    }
}
