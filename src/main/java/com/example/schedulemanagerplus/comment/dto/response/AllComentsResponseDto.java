package com.example.schedulemanagerplus.comment.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Getter
@RequiredArgsConstructor
public class AllComentsResponseDto {
    private final List<CommentResponseDto> comments;
    private final int totalCount;
    private final int totalPages;
    private final int currentPage;
}
