package com.example.jpalearnapp.controller;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository repository;

    @GetMapping("/createMember")
    public Member saveMember() {
        Member member = repository.save(new Member("steve"));
        System.out.println(member.getUserName());
        return member;
    }
}
