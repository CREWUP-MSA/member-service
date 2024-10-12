package com.example.memberservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	EMAIL_ALREADY_EXISTS("이미 사용중인 이메일입니다.", 400),

	MEMBER_NOT_FOUND("해당 회원을 찾을 수 없습니다.", 404)
	;

	private final String message;
	private final int status;

	ErrorCode(String message, int status) {
		this.message = message;
		this.status = status;
	}
}
