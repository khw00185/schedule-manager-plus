package com.example.schedulemanagerplus.schedule.service;

import com.example.schedulemanagerplus.comment.repository.CommentRepository;
import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.jwt.entity.AuthMember;
import com.example.schedulemanagerplus.member.entity.Member;
import com.example.schedulemanagerplus.member.exception.UserNotFoundException;
import com.example.schedulemanagerplus.member.repository.MemberRepository;
import com.example.schedulemanagerplus.schedule.dto.request.ScheduleRequestDto;
import com.example.schedulemanagerplus.schedule.dto.response.AllSchedulesResponseDto;
import com.example.schedulemanagerplus.schedule.dto.response.ScheduleResponseDto;
import com.example.schedulemanagerplus.schedule.entity.Schedule;
import com.example.schedulemanagerplus.schedule.exception.ScheduleNotFoundException;
import com.example.schedulemanagerplus.schedule.exception.SchedulePermissionDenied;
import com.example.schedulemanagerplus.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public ResponseDto<AllSchedulesResponseDto> getAllSchedules(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Page<Schedule> schedulePage = scheduleRepository.findAllByOrderByModifiedAtDesc(pageRequest);

        List<ScheduleResponseDto> scheduleDto = schedulePage.getContent().stream()
                .map(schedule -> {
                    int commentCount =  commentRepository.countByScheduleId(schedule.getId());
                    return new ScheduleResponseDto(
                            schedule.getId(),
                            schedule.getTitle(),
                            schedule.getContent(),
                            commentCount,
                            schedule.getMember().getName()
                    );
                })
                .toList();

        AllSchedulesResponseDto responseDto = new AllSchedulesResponseDto(
                scheduleDto,
                (int) schedulePage.getTotalElements(),
                schedulePage.getTotalPages(),
                schedulePage.getNumber() + 1
        );
        return ResponseDto.success(responseDto);
    }

    public ResponseDto<ScheduleResponseDto> getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);
        int commentCount =  commentRepository.countByScheduleId(schedule.getId());

        ScheduleResponseDto response = new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                commentCount,
                schedule.getMember().getName()
        );
        return ResponseDto.success(response);
    }

    public ResponseDto<ScheduleResponseDto> saveSchedule(ScheduleRequestDto request) {
        AuthMember memberInfo = getCurrentMemberInfo();

        Member member = memberRepository.findById(memberInfo.getUserId()).orElseThrow(UserNotFoundException::new);

        Schedule schedule = new Schedule(request, member);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        ScheduleResponseDto response = new ScheduleResponseDto(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                0,
                savedSchedule.getMember().getName()
        );
        return ResponseDto.success(response);
    }

    public ResponseDto<ScheduleResponseDto> updateSchedule(Long id, ScheduleRequestDto request) {
        AuthMember memberInfo = getCurrentMemberInfo();
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(ScheduleNotFoundException::new);

        validateScheduleOwner(schedule, memberInfo.getUserId());

        schedule.update(request.getTitle(), request.getContent());
        Schedule savedSchedule = scheduleRepository.save(schedule);
        int commentCount =  commentRepository.countByScheduleId(savedSchedule.getId());
        ScheduleResponseDto response = new ScheduleResponseDto(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                commentCount,
                schedule.getMember().getName()
        );

        return ResponseDto.success(response);
    }

    public ResponseDto<String> deleteSchedule(Long id) {
        AuthMember memberInfo = getCurrentMemberInfo();
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(ScheduleNotFoundException::new);

        validateScheduleOwner(schedule, memberInfo.getUserId());

        scheduleRepository.delete(schedule);
        return ResponseDto.success("성공적으로 삭제되었습니다.");
    }

    private AuthMember getCurrentMemberInfo() {
        AuthMember authMember = (AuthMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authMember;
    }

    private void validateScheduleOwner(Schedule schedule, String userId) {
        if (!schedule.getMember().getId().equals(userId)) {
            throw new SchedulePermissionDenied();
        }
    }
}
