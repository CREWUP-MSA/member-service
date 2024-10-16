package com.example.memberservice.dto.request;

public record AuthenticateRequest(
    String email,
    String password
) {
}
