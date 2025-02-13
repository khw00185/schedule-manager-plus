package com.example.schedulemanagerplus.comment.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
    private final Long id;
    private final String memberName;
    private final String content;

}
