package com.example.memberservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memberservice.dto.RegisterRequest;
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
	public void register(RegisterRequest request) {
		if (memberRepository.existsByEmail(request.email()))
			throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);

		memberRepository.save(request.toEntity());
	}
}
