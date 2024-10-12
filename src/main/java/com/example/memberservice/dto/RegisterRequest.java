package com.example.memberservice.dto;


import com.example.memberservice.entity.Member;
import com.example.memberservice.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	String email,

	@NotBlank(message = "이름을 입력해주세요.")
	String name,

	@NotBlank(message = "닉네임을 입력해주세요.")
	String nickname,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	String password
) {

	public Member toEntity(){
		Member member = Member.builder()
			.email(email())
			.name(name())
			.password(password())
			.role(Role.ROLE_USER)
			.build();

		return member;
	}
}
