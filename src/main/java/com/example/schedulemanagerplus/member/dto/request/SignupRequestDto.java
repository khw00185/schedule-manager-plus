package com.example.schedulemanagerplus.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private final String email;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private final String name;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상으로 설정해주세요.")
    private final String password;

}
