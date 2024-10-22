package com.example.memberservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomApiResponse<T>(
    String message,
    T data
) {
    public static <T> CustomApiResponse<T> success(T data) {
        return new CustomApiResponse<>(null, data);
    }

    public static <T> CustomApiResponse<T> success(String message, T data) {
        return new CustomApiResponse<>(message, data);
    }

    public static <T> CustomApiResponse<T> fail(String message) {
        return new CustomApiResponse<>(message, null);
    }

    public static <T> CustomApiResponse<T> fail(T data) {
        return new CustomApiResponse<>(null, data);
    }
}
