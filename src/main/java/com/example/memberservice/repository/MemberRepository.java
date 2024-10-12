package com.example.memberservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.memberservice.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);
}
