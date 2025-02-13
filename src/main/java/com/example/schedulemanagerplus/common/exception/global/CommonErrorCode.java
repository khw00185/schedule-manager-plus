package com.example.schedulemanagerplus.common.exception.global;

import com.example.schedulemanagerplus.common.exception.BaseErrorCode;
import com.example.schedulemanagerplus.common.exception.dto.ErrorReason;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@Getter
public enum CommonErrorCode implements BaseErrorCode {

    //schedule관련
    SchedulePermissionDenied(HttpStatus.FORBIDDEN, "SCHEDULE_001", "작성자의 권한이 없습니다."),
    ScheduleNotFound(HttpStatus.NOT_FOUND, "SCHEDULE_002", "해당 일정이 존재하지 않습니다."),
    AuthorNotFound(HttpStatus.NOT_FOUND, "SCHEDULE_003", "해당 작성자의 일정이 존재하지 않습니다."),

    //comment관련
    CommentPermissionDenied(HttpStatus.FORBIDDEN, "COMMENT_001", "작성자의 권한이 없습니다."),
    CommentNotFound(HttpStatus.NOT_FOUND, "COMMENT_002", "해당 댓글이 존재하지 않습니다."),

    //user관련
    InvalidUserId(HttpStatus.UNAUTHORIZED, "USER_001", "ID가 일치하지 않습니다."),
    InvalidPassword(HttpStatus.UNAUTHORIZED, "USER_002", "비밀번호가 일치하지 않습니다."),
    DuplicatedId(HttpStatus.CONFLICT, "USER_003", "이미 존재하는 Email입니다."),
    UserNotFound(HttpStatus.NOT_FOUND, "USER_004", "존재하지 않는 유저입니다."),

    //jwt토큰 관련
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_001", "Access Token이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_002", "Refresh Token이 만료되었습니다."),
    InvalidTokenFormat(HttpStatus.BAD_REQUEST, "AUTH_003", "잘못된 형식의 토큰입니다."),
    AttemptAuthentication(HttpStatus.UNAUTHORIZED, "AUTH_004", "로그인 인증 실패"),
    NotFoundToken(HttpStatus.NOT_FOUND, "AUTH_005", "토큰이 존재하지 않습니다."),
    RefreshTokenMismatch(HttpStatus.UNAUTHORIZED, "AUTH_006", "비정상적인 요청입니다. 리프레쉬 토큰이 일치하지 않습니다."),


    //요청 관련
    RequestBodyRead(HttpStatus.BAD_REQUEST, "REQUEST_001", "요청 본문을 읽는 중 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "REQUEST_002", "입력값이 유효하지 않습니다."),

    // 내부 서버 오류 관련
    InternalServerError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_001", "예상치 못한 서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String errorMessage;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status,code,errorMessage);
    }
}
