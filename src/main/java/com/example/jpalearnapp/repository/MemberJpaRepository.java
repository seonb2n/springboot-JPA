package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
