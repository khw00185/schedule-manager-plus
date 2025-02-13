package com.example.schedulemanagerplus.comment.exception;

import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;

public class CommentPermissionDeniedException extends CustomException {
    public CommentPermissionDeniedException() {
        super(CommonErrorCode.CommentPermissionDenied);
    }
}
