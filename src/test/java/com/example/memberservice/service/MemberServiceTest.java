package com.example.memberservice.service;

import com.example.memberservice.dto.request.AuthenticateRequest;
import com.example.memberservice.dto.request.RegisterRequest;
import com.example.memberservice.dto.response.MemberResponse;
import com.example.memberservice.entity.Member;
import com.example.memberservice.exception.CustomException;
import com.example.memberservice.exception.ErrorCode;
import com.example.memberservice.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EventProducer eventProducer;

    @InjectMocks private MemberService memberService;

    private final Member member = Member.builder()
            .email("test@example.com")
            .password("encodedPassword")
            .name("test user")
            .build();

    @Test
    @DisplayName("회원 가입 테스트")
    void register() {
        RegisterRequest request = new RegisterRequest("test@example.com", "password", "test user");

        when(memberRepository.existsByEmail(request.email())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.register(request);

        assertNotNull(response);
        verify(memberRepository).existsByEmail(request.email());
        verify(memberRepository).save(any(Member.class));
        verify(eventProducer).sendMemberCreateEvent(member.getId());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이미 존재하는 이메일")
    void register_fail () {
        RegisterRequest request = new RegisterRequest("test@example.com", "password", "test user");

        when(memberRepository.existsByEmail(request.email())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> memberService.register(request));

        assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_ALREADY_EXISTS);
        verify(memberRepository, never()).save(member);
    }

    @Test
    @DisplayName("회원 조회 - By ID")
    void findMemberById() {
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findMemberById(memberId);

        assertNotNull(response);
        verify(memberRepository).findById(memberId);
    }

    @Test
    @DisplayName("회원 조회 By ID 실패 - 회원을 찾을 수 없는 경우")
    void findMemberById_fail_memberNotFound() {
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> memberService.findMemberById(memberId));

        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);
        verify(memberRepository).findById(memberId);
    }

    @Test
    @DisplayName("회원 조회 - By Email")
    void findMemberByEmail() {
        String email = "test@example.com";

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findMemberByEmail(email);

        assertNotNull(response);
        verify(memberRepository).findByEmail(email);
    }

    @Test
    @DisplayName("회원 조회 By Email 실패 - 회원을 찾을 수 없는 경우")
    void findMemberByEmail_fail_memberNotFound() {
        String email = "test@example.com";

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> memberService.findMemberByEmail(email));

        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);
        verify(memberRepository).findByEmail(email);
    }

    @Test
    @DisplayName("회원 인증 테스트")
    void authenticate() {
        AuthenticateRequest request = new AuthenticateRequest("test@example.com", "password");

        when(memberRepository.findByEmail(request.email())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(request.password(), member.getPassword())).thenReturn(true);

        boolean result = memberService.authenticate(request);

        assertTrue(result);
        verify(memberRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), member.getPassword());
    }

    @Test
    @DisplayName("회원 인증 실패 - 회원을 찾을 수 없는 경우")
    void authenticate_fail_memberNotFound() {
        AuthenticateRequest request = new AuthenticateRequest("test@example.com", "password");

        when(memberRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> memberService.authenticate(request));

        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);
        verify(memberRepository).findByEmail(request.email());
        verify(passwordEncoder, never()).matches(request.email(), member.getEmail());
    }

    @Test
    @DisplayName("회원 인증 실패 - 비밀번호가 일치하지 않는 경우")
    void authenticate_fail_passwordNotMatched() {
        AuthenticateRequest request = new AuthenticateRequest("test@example.com", "password");

        when(memberRepository.findByEmail(request.email())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(request.password(), member.getPassword())).thenReturn(false);

        boolean result = memberService.authenticate(request);

        assertFalse(result);
        verify(memberRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), member.getPassword());
    }
}