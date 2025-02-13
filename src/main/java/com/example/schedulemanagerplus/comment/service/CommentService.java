package com.example.schedulemanagerplus.comment.service;

import com.example.schedulemanagerplus.comment.dto.request.CommentRequestDto;
import com.example.schedulemanagerplus.comment.dto.response.AllComentsResponseDto;
import com.example.schedulemanagerplus.comment.dto.response.CommentResponseDto;
import com.example.schedulemanagerplus.comment.entity.Comment;
import com.example.schedulemanagerplus.comment.exception.CommentNotFoundException;
import com.example.schedulemanagerplus.comment.exception.CommentPermissionDeniedException;
import com.example.schedulemanagerplus.comment.repository.CommentRepository;
import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.jwt.entity.AuthMember;
import com.example.schedulemanagerplus.member.entity.Member;
import com.example.schedulemanagerplus.member.exception.UserNotFoundException;
import com.example.schedulemanagerplus.member.repository.MemberRepository;
import com.example.schedulemanagerplus.schedule.entity.Schedule;
import com.example.schedulemanagerplus.schedule.exception.ScheduleNotFoundException;
import com.example.schedulemanagerplus.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;


    public ResponseDto<AllComentsResponseDto> getAllComents(Long scheduleId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Page<Comment> commnetPage = commentRepository.findAllByScheduleIdOrderByModifiedAtDesc(scheduleId, pageRequest);

        List<CommentResponseDto> commentDtos = commnetPage.getContent().stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getMember().getName(),
                        comment.getContent()
                ))
                .toList();


        AllComentsResponseDto responseDto = new AllComentsResponseDto(
                commentDtos,
                (int) commnetPage.getTotalElements(),
                commnetPage.getTotalPages(),
                commnetPage.getNumber() + 1
        );

        return ResponseDto.success(responseDto);
    }

    public ResponseDto<CommentResponseDto> saveComment(Long scheduleId, CommentRequestDto requestDto) {
        AuthMember memberInfo = getCurrentMemberInfo();

        Member member = memberRepository.findById(memberInfo.getUserId())
                .orElseThrow(UserNotFoundException::new);
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);

        Comment comment = new Comment(
                schedule,
                member,
                requestDto.getContent()
        );
        Comment savedComment = commentRepository.save(comment);
        CommentResponseDto responseDto = new CommentResponseDto(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getMember().getName()
        );
        return ResponseDto.success(responseDto);
    }

    public ResponseDto<CommentResponseDto> updateComment(Long commentId, CommentRequestDto requestDto) {
        AuthMember memberInfo = getCurrentMemberInfo();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        validateCommentOwner(comment, memberInfo.getUserId());

        comment.update(requestDto.getContent());
        Comment updatedComment = commentRepository.save(comment);

        CommentResponseDto response = new CommentResponseDto(
                updatedComment.getId(),
                updatedComment.getMember().getName(),
                updatedComment.getContent()
        );

        return ResponseDto.success(response);

    }


    public ResponseDto<String> deleteComment(Long commentId) {
        AuthMember memberInfo = getCurrentMemberInfo();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        validateCommentOwner(comment, memberInfo.getUserId());

        commentRepository.delete(comment);
        return ResponseDto.success("성공적으로 삭제되었습니다.");
    }

    private AuthMember getCurrentMemberInfo() {
        AuthMember authMember = (AuthMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authMember;
    }

    private void validateCommentOwner(Comment comment, String userId){
        if (!comment.getMember().getId().equals(userId)) {
            throw new CommentPermissionDeniedException();
        }
    }

}
