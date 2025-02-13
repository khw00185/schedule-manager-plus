package com.example.schedulemanagerplus.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AllSchedulesResponseDto {
    private final List<ScheduleResponseDto> schedules;
    private final int totalCount;
    private final int totalPages;
    private final int currentPage;
}
