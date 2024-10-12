package com.example.memberservice.controller;

import com.example.memberservice.dto.ApiResponse;
import com.example.memberservice.dto.response.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.memberservice.dto.request.RegisterRequest;
import com.example.memberservice.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-service/api")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/member")
	public ResponseEntity<ApiResponse<MemberResponse>> createMember(
			@RequestBody RegisterRequest request) {

		return ResponseEntity.ok(ApiResponse.success(memberService.register(request)));
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable Long memberId) {
		return ResponseEntity.ok(ApiResponse.success(memberService.findMember(memberId)));
	}
}
