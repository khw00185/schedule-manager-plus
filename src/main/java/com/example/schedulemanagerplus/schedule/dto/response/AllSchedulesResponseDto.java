package com.example.schedulemanagerplus.schedule.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class AllSchedulesResponseDto {
    private final List<ScheduleResponseDto> schedules;
    private final int totalCount;
    private final int totalPages;
    private final int currentPage;
}
