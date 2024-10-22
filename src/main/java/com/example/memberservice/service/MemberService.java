package com.example.memberservice.service;

import com.example.memberservice.dto.request.AuthenticateRequest;
import com.example.memberservice.dto.response.MemberResponse;
import com.example.memberservice.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memberservice.dto.request.RegisterRequest;
import com.example.memberservice.exception.CustomException;
import com.example.memberservice.exception.ErrorCode;
import com.example.memberservice.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final EventProducer eventProducer;

	/**
	 * 회원 가입
	 * @param request 회원가입 요청 정보
	 * @return MemberResponse
	 * @throws CustomException 이미 존재하는 이메일일 경우
	 * @see ErrorCode
	 */
	@Transactional
	public MemberResponse register(RegisterRequest request) {
		if (memberRepository.existsByEmail(request.email()))
			throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);

		Member member = memberRepository.save(request.toEntity(passwordEncoder));

		log.info("member registered: {}", member);
		eventProducer.sendMemberCreateEvent(member.getId());
		return MemberResponse.from(member);
	}

	/**
	 * 회원 조회 - By ID
	 * @param id 회원 ID
	 * @return MemberResponse
	 * @throws CustomException 회원을 찾을 수 없는 경우
	 * @see ErrorCode
	 */
	public MemberResponse findMemberById(Long id) {
		Member member = memberRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberResponse.from(member);
	}

	/**
	 * 회원 조회 - By Email
	 * @param email 회원 이메일
	 * @return MemberResponse
	 * @throws CustomException 회원을 찾을 수 없는 경우
	 * @see ErrorCode
	 */
	public MemberResponse findMemberByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberResponse.from(member);
	}

	/**
	 * 회원 인증
	 * @param request 인증 요청 정보
	 * @return Boolean 인증 성공 여부
	 * @throws CustomException 회원을 찾을 수 없는 경우
	 * @see ErrorCode
	 */
	public boolean authenticate(AuthenticateRequest request) {
		Member member = memberRepository.findByEmail(request.email())
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return passwordEncoder.matches(request.password(), member.getPassword());
	}
}
