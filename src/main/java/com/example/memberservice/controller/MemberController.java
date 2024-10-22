package com.example.memberservice.controller;

import com.example.memberservice.config.swagger.EmailAlreadyExistsApiResponse;
import com.example.memberservice.config.swagger.MemberNotFoundApiResponse;
import com.example.memberservice.dto.CustomApiResponse;
import com.example.memberservice.dto.request.AuthenticateRequest;
import com.example.memberservice.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.memberservice.dto.request.RegisterRequest;
import com.example.memberservice.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-service/api")
@Tag(name = "회원 API", description = "회원 관련 API")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/member")
	@Operation(summary = "회원가입", description = "회원을 가입시킵니다.")
	@ApiResponse(responseCode = "200", description = "회원가입 성공")
	@EmailAlreadyExistsApiResponse
	public ResponseEntity<CustomApiResponse<MemberResponse>> createMember(
			@RequestBody @Valid RegisterRequest request) {

		return ResponseEntity.ok(CustomApiResponse.success(memberService.register(request)));
	}

	@GetMapping("/member/by-id")
	@Operation(summary = "회원 조회 - ID", description = "ID로 회원을 조회합니다.")
	@MemberNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<MemberResponse>> getMemberById(@RequestParam("id") Long id) {
		return ResponseEntity.ok(CustomApiResponse.success(memberService.findMemberById(id)));
	}

	@GetMapping("/member/by-email")
	@Operation(summary = "회원 조회 - Email", description = "Email로 회원을 조회합니다.")
	@MemberNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<MemberResponse>> getMemberByEmail(@RequestParam("email") String email) {
		return ResponseEntity.ok(CustomApiResponse.success(memberService.findMemberByEmail(email)));
	}

	@PostMapping("/member/authenticate")
	@MemberNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<Boolean>> authenticateMember(@RequestBody AuthenticateRequest request) {
		return ResponseEntity.ok(CustomApiResponse.success(memberService.authenticate(request)));
	}
}
