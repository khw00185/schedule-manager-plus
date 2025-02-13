package com.example.schedulemanagerplus.member.controller;

import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.jwt.dto.TokenDto;
import com.example.schedulemanagerplus.member.dto.request.DeleteAccountRequestDto;
import com.example.schedulemanagerplus.member.dto.request.SigninRequestDto;
import com.example.schedulemanagerplus.member.dto.request.SignupRequestDto;
import com.example.schedulemanagerplus.member.dto.request.UpdateMemberRequestDto;
import com.example.schedulemanagerplus.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<?>> signup(@Valid @RequestBody SignupRequestDto request){
        return ResponseEntity.ok(memberService.signUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseDto<?>> signin(@Valid @RequestBody SigninRequestDto request, HttpServletResponse response){
        TokenDto tokenDto = memberService.signin(request);
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Set-Cookie", "Refresh-Token="+ tokenDto.getRefreshToken() + "; path=/; HttpOnly; SameSite=Strict"); //Secure; 테스트를 위해 제거

        return ResponseEntity.ok(ResponseDto.success("로그인 성공"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<?>> logout(){
        return ResponseEntity.ok(memberService.logout());
    }


    @PutMapping("/update")
    public ResponseEntity<ResponseDto<?>> updateMember(@Valid @RequestBody UpdateMemberRequestDto request){
        return ResponseEntity.ok(memberService.updateMember(request));
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<ResponseDto<?>> deleteAccount(@RequestBody DeleteAccountRequestDto request){
        return ResponseEntity.ok(memberService.deleteAccount(request));
    }
}
//자자  이제 로그아웃 문제 다듬고, 유저정보 변경, 회원 탈퇴, 스케줄 단건 조회 해보면 됨. ㅎㅎ
