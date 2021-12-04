package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.dto.MemberDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    TeamJpaRepository teamJpaRepository;

    private RepositoryTestHelper repositoryTestHelper;

    @BeforeEach
    protected void before() {
        repositoryTestHelper = new RepositoryTestHelper(memberJpaRepository, teamJpaRepository);
        repositoryTestHelper.initDB();
    }

    @Test
    public void testMember() throws Exception {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        Optional<Member> byId = memberJpaRepository.findById(savedMember.getId());

        assertThat(byId.get().getId()).isEqualTo(member.getId());
    }

    @Test
    public void findByUserNameAndAgeGreaterThan() throws Exception {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUserNameAndAgeGreaterThan("BBB", 15);
        AssertionsForClassTypes.assertThat(result.get(0).getUserName()).isEqualTo("BBB");
    }

    @Test
    public void findMemberDtoTest() throws Exception {
        List<MemberDto> memberDto = memberJpaRepository.findMemberDto();
        memberDto.forEach(System.out::println);
    }

    @Test
    public void findMembersByNames() throws Exception {
        List<String> names = new ArrayList<>();
        names.add("aaa");
        names.add("bbb");
        names.add("ccc");
        List<Member> result = memberJpaRepository.findByNames(names);
        assertThat(result.size()).isEqualTo(3);
    }
}