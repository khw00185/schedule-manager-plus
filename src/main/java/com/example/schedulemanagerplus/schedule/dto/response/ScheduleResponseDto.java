package com.example.schedulemanagerplus.schedule.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleResponseDto {
    private final Long id;

    private final String title;

    private final String content;

    private final int commentCount;

    private final String memberName;
}
