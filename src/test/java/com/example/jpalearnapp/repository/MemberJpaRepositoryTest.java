package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.dto.MemberDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    TeamJpaRepository teamJpaRepository;
    @PersistenceContext
    EntityManager em;

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
        result.get(0).setUserName("AAA");
        Optional<Member> aaa = memberJpaRepository.findByUserName("AAA");
        assertThat(aaa.get().getUserName()).isEqualTo("AAA");
    }

    @Test
    public void paging() throws Exception {

        int age = 20;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        Page<Member> page = memberJpaRepository.findByAge(age, pageRequest);

        //page 를 dto 로 변환하는 방법
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getUserName(), member.getAge(), member.getTeam().getName()));

        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getNumber()).isEqualTo(0); //페이지의 번호를 가져온다
        assertThat(page.getTotalPages()).isEqualTo(2); //페이지의 전체 쪽수
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void slicing() throws Exception {
        int age = 20;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        Slice<Member> slice = memberJpaRepository.findByAge(age, pageRequest);
        //limit 를 4개로 가져옴.
        List<Member> content = slice.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(slice.getNumber()).isEqualTo(0);
    }

    @Test
    public void bulkAgePlusTest() throws Exception {
        int age = 20;
        int result = memberJpaRepository.bulkAgePlus(age);
//        em.flush();
//        em.clear();
        Optional<Member> bbb = memberJpaRepository.findByUserName("bbb");
        System.out.println("bbb.get().getAge() = " + bbb.get().getAge());

        assertThat(result).isEqualTo(2);
    }

    @Test
    public void fetchJoinTest() throws Exception {
        List<Member> memberUsingFetchJoin = memberJpaRepository.findAll();
        for (Member member : memberUsingFetchJoin) {
            System.out.println("member.getUserName() = " + member.getUserName());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void findMemberWithEntityGraphTest() throws Exception {
        List<Member> memberEntityGraph = memberJpaRepository.findMemberEntityGraph();
        for (Member member : memberEntityGraph) {
            System.out.println("member = " + member.getUserName());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHintTest() throws Exception {
        Member aaa = memberJpaRepository.findReadOnlyByUserName("aaa");
        aaa.setUserName("AAA");
    }

    @Test
    public void lockTest() throws Exception {
        Member aaa = memberJpaRepository.findLockByUserName("aaa");

    }

    @Test
    public void callCustomTest() throws Exception {
        List<Member> memberCustom = memberJpaRepository.findMemberCustom();
    }

    @Test
    public void projections() throws Exception {
        List<UsernameOnly> result = memberJpaRepository.findProjectionsByUserName("aaa");
        //인터페이스만 만들면 spring data jpa가 구현체를 만들어줌
        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly.getUserName());
        }
    }

    //구체적 class 를 만들어도 된다.
    @Test
    public void projections2() throws Exception {
        List<UserNameOnlyDto> result = memberJpaRepository.findProjections2ByUserName("aaa");
        for (UserNameOnlyDto userNameOnlyDto : result) {
            System.out.println("userNameOnlyDto = " + userNameOnlyDto.getUserName());
        }
    }


    @Test
    public void nativeQuery() throws Exception {
        Page<MemberProjection> byNativeProection = memberJpaRepository.findByNativeProection(PageRequest.of(0, 10));
        List<MemberProjection> result = byNativeProection.getContent();
        for (MemberProjection memberProjection : result) {
            System.out.println("memberProjection = " + memberProjection.getUserName());
        }
    }

}