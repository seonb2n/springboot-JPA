package com.example.jpalearnapp.controller;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.dto.MemberDto;
import com.example.jpalearnapp.repository.MemberJpaRepository;
import com.example.jpalearnapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository repository;
    private final MemberJpaRepository memberRepository;

    @GetMapping("/createMember")
    public Member saveMember() {
        Member member = repository.save(new Member("steve"));
        System.out.println(member.getUserName());
        return member;
    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Optional<Member> member = repository.findById(id);
        return member.get().getUserName();
    }


    //스프링이 중간에서 파라미터로 바로 멤버로 injecting 해줌
    //도메인 클래스 컨버터
    //조회용으로만 사용해야 한다. 트랜잭션이 없이 멤버를 조회했으므로, 엔티티 변경이 DB에 반영 안됨
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUserName();
    }

    //localhost:8080/members?page=10&size=3&sort=id,desc&sort=username,desc
    // page 단위로 호출 할 수 있음. page size 도 지정 가능. sort 도 가능. Pageable 을 인자로 받음으로서
    //application.yml 에서 global 설정 가능
    //@PageableDefault 사용해서 설정도 가능
    //접두사 사용 가능
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> map = page.map(member -> new MemberDto(member.getUserName(), member.getAge(), null));
        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }




    @PostConstruct
    public void init() {
        for (int i = 0; i<100; i++) {
            memberRepository.save(new Member("user"+i, i));
        }
    }
}
