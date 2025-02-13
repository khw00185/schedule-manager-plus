package com.example.schedulemanagerplus.member.service;

import com.example.schedulemanagerplus.common.dto.ResponseDto;
import com.example.schedulemanagerplus.common.exception.CustomException;
import com.example.schedulemanagerplus.common.exception.global.CommonErrorCode;
import com.example.schedulemanagerplus.jwt.JwtUtil;
import com.example.schedulemanagerplus.jwt.dto.TokenDto;
import com.example.schedulemanagerplus.jwt.entity.AuthMember;
import com.example.schedulemanagerplus.jwt.entity.RefreshToken;
import com.example.schedulemanagerplus.jwt.exception.NotFoundTokenException;
import com.example.schedulemanagerplus.jwt.repository.RefreshTokenRepository;
import com.example.schedulemanagerplus.member.dto.request.DeleteAccountRequestDto;
import com.example.schedulemanagerplus.member.dto.request.SigninRequestDto;
import com.example.schedulemanagerplus.member.dto.request.SignupRequestDto;
import com.example.schedulemanagerplus.member.dto.request.UpdateMemberRequestDto;
import com.example.schedulemanagerplus.member.dto.response.MemberResponseDto;
import com.example.schedulemanagerplus.member.entity.Member;
import com.example.schedulemanagerplus.member.exception.DuplicatedIdException;
import com.example.schedulemanagerplus.member.exception.InvalidPasswordException;
import com.example.schedulemanagerplus.member.exception.UserNotFoundException;
import com.example.schedulemanagerplus.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseDto<MemberResponseDto> signUp(SignupRequestDto dto) {
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new DuplicatedIdException();
        }
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getEmail(), dto.getName(), encryptedPassword);

        Member savedMember = memberRepository.save(member);
        MemberResponseDto response = new MemberResponseDto(savedMember.getEmail(), savedMember.getName(), savedMember.getCreatedAt(), savedMember.getModifiedAt());

        return ResponseDto.success(response);
    }

    @Transactional
    public TokenDto signin(SigninRequestDto dto) {
        try {
            Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(UserNotFoundException::new);
            if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())){
                    throw new InvalidPasswordException();
            }

            TokenDto tokenDto = jwtUtil.createToken(member.getId());

            refreshTokenRepository.findByMemberId(member.getId())
                    .ifPresentOrElse(
                            existingToken -> existingToken.updateToken(tokenDto.getRefreshToken()), // 업데이트
                            () -> refreshTokenRepository.save(new RefreshToken(tokenDto.getRefreshToken(), member)) // 새로 저장
                    );

            return tokenDto;

        } catch (UserNotFoundException | BadCredentialsException e) {
            log.warn("로그인 실패: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("예기치 못한 오류 발생", e);
            throw new CustomException(CommonErrorCode.InternalServerError);
        }

    }

    @Transactional
    public ResponseDto<String> logout(){
        AuthMember authMember = getCurrentMemberInfo();

        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(authMember.getUserId())
                .orElseThrow(NotFoundTokenException::new);
        refreshTokenRepository.delete(refreshToken);



        return ResponseDto.success("로그아웃이 완료되었습니다.");
    }

    @Transactional
    public ResponseDto<MemberResponseDto> updateMember(UpdateMemberRequestDto dto) {
        AuthMember authMember = getCurrentMemberInfo();

        Member member = memberRepository.findById(authMember.getUserId()).orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())){
            throw new InvalidPasswordException();
        }

        member.updateName(dto.getName());
        member.updatePassword(passwordEncoder.encode(dto.getNewPassword()));

        MemberResponseDto response = new MemberResponseDto(member.getEmail(), member.getName(), member.getCreatedAt(), member.getModifiedAt());

        return ResponseDto.success(response);
    }

    @Transactional
    public ResponseDto<String> deleteAccount(DeleteAccountRequestDto request) {
        AuthMember authMember = getCurrentMemberInfo();

        Member member = memberRepository.findById(authMember.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        // 리프레시 토큰 먼저 삭제
        refreshTokenRepository.findByMemberId(member.getId())
                .ifPresent(refreshTokenRepository::delete);

        memberRepository.delete(member);
        return ResponseDto.success("회원탈퇴가 완료되었습니다.");
    }



    private AuthMember getCurrentMemberInfo() {
        AuthMember authMember = (AuthMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authMember;
    }
}
