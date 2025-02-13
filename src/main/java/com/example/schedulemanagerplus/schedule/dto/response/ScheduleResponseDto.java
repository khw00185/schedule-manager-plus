package com.example.schedulemanagerplus.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private final Long id;

    private final String title;

    private final String content;

    private final int commentCount;

    private final String memberName;
}
