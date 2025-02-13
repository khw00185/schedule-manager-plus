package com.example.schedulemanagerplus.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @NotBlank(message = "일정 제목은 필수 입력값입니다.")
    private String title;

    @NotBlank(message = "일정 내용은 필수 입력값입니다.")
    private String content;
}
