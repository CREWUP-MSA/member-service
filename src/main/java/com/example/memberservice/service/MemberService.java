package com.example.memberservice.service;

import com.example.memberservice.dto.response.MemberResponse;
import com.example.memberservice.entity.Member;
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
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public MemberResponse register(RegisterRequest request) {
		if (memberRepository.existsByEmail(request.email()))
			throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);

		Member member = memberRepository.save(request.toEntity());

		return MemberResponse.from(member);
	}

	public MemberResponse findMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberResponse.from(member);
	}
}
