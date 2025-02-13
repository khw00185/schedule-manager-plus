package com.example.schedulemanagerplus.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateMemberRequestDto {

    @Size(min = 2, max = 20, message = "이름은 2~20자 사이여야 합니다.")
    @NotBlank(message = "공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "공백일 수 없습니다.")
    private String currentPassword;

    @NotBlank(message = "공백일 수 없습니다.")
    @Size(min=8, message = "비밀번호는 8자 이상이여야 합니다.")
    private String newPassword;
}
