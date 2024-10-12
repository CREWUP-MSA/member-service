package com.example.memberservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	EMAIL_ALREADY_EXISTS("이미 사용중인 이메일입니다.", 400),
	;

	private final String message;
	private final int status;

	ErrorCode(String message, int status) {
		this.message = message;
		this.status = status;
	}
}
