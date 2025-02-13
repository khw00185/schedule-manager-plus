package com.example.schedulemanagerplus.schedule.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class SchedulePermissionDenied extends CustomException {
    public SchedulePermissionDenied() {
        super(CommonErrorCode.SchedulePermissionDenied);
    }
}
