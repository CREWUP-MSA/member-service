package com.example.memberservice.service;

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

    @InjectMocks private MemberService memberService;

    @Test
    @DisplayName("회원 가입 테스트")
    void register() {
        RegisterRequest request = mock(RegisterRequest.class);
        Member member = mock(Member.class);

        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(memberRepository.save(any())).thenReturn(member);

        MemberResponse response = memberService.register(request);

        assertNotNull(response);
        verify(memberRepository).existsByEmail(any());
        verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("회원 가입 실패 - 이미 존재하는 이메일")
    void register_fail () {
        RegisterRequest request = mock(RegisterRequest.class);

        when(memberRepository.existsByEmail(any())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> memberService.register(request));

        assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_ALREADY_EXISTS);
        verify(memberRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 조회 - By ID")
    void findMemberById() {
        Member member = mock(Member.class);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findMemberById(anyLong());

        assertNotNull(response);
        verify(memberRepository).findById(any());
    }

    @Test
    @DisplayName("회원 조회 By ID 실패 - 회원을 찾을 수 없는 경우")
    void findMemberById_fail_memberNotFound() {
        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> memberService.findMemberById(anyLong()));

        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);
        verify(memberRepository).findById(any());
    }

    @Test
    @DisplayName("회원 조회 - By Email")
    void findMemberByEmail() {
        Member member = mock(Member.class);

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findMemberByEmail(anyString());

        assertNotNull(response);
        verify(memberRepository).findByEmail(anyString());
    }

    @Test
    @DisplayName("회원 조회 By Email 실패 - 회원을 찾을 수 없는 경우")
    void findMemberByEmail_fail_memberNotFound() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> memberService.findMemberByEmail(anyString()));

        assertEquals(exception.getErrorCode(), ErrorCode.MEMBER_NOT_FOUND);
        verify(memberRepository).findByEmail(anyString());
    }
}