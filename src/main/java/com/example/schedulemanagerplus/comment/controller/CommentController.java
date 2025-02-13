package com.example.schedulemanagerplus.comment.controller;

import com.example.schedulemanagerplus.comment.dto.request.CommentRequestDto;
import com.example.schedulemanagerplus.comment.service.CommentService;
import com.example.schedulemanagerplus.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto<?>> getComments(
            @PathVariable Long scheduleId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        return ResponseEntity.ok(commentService.getAllComents(scheduleId, pageNumber, pageSize));
    }

    @PostMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto<?>> saveComment(@PathVariable Long scheduleId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.saveComment(scheduleId, requestDto));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseDto<?>> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.updateComment(commentId, requestDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto<?>> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }


}
