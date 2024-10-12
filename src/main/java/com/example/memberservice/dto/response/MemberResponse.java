package com.example.memberservice.dto.response;

import com.example.memberservice.entity.Member;
import com.example.memberservice.entity.Role;

public record MemberResponse(
    Long id,
    String email,
    String name,
    String provider,
    Role role,
    boolean isDeleted
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getProvider(),
            member.getRole(),
            member.isDeleted()
        );
    }
}
