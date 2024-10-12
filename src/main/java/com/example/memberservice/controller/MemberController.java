package com.example.memberservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.memberservice.dto.RegisterRequest;
import com.example.memberservice.exception.CustomException;
import com.example.memberservice.exception.ErrorCode;
import com.example.memberservice.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			memberService.register(request);
			return ResponseEntity.ok().build();

		} catch (CustomException e) {

			ErrorCode errorCode = e.getErrorCode();
			return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getMessage());
		}
	}
}
