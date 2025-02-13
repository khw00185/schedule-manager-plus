package com.example.schedulemanagerplus.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class AllComentsResponseDto {
    private final List<CommentResponseDto> comments;
    private final int totalCount;
    private final int totalPages;
    private final int currentPage;
}
