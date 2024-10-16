package com.example.memberservice.controller;

import com.example.memberservice.dto.ApiResponse;
import com.example.memberservice.dto.request.AuthenticateRequest;
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

	@GetMapping("/member/by-id")
	public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@RequestParam("id") Long id) {
		return ResponseEntity.ok(ApiResponse.success(memberService.findMemberById(id)));
	}

	@GetMapping("/member/by-email")
	public ResponseEntity<ApiResponse<MemberResponse>> getMemberByEmail(@RequestParam("email") String email) {
		return ResponseEntity.ok(ApiResponse.success(memberService.findMemberByEmail(email)));
	}

	@PostMapping("/member/authenticate")
	public ResponseEntity<ApiResponse<Boolean>> authenticateMember(@RequestBody AuthenticateRequest request) {
		return ResponseEntity.ok(ApiResponse.success(memberService.authenticate(request)));
	}
}
