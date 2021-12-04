package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserNameAndAgeGreaterThan(String username, int age);

    @Query("select new com.example.jpalearnapp.entity.dto.MemberDto(m.userName, m.age, t.name) " +
            "from Member m " +
            "join m.team t")
    List<MemberDto> findMemberDto();

    Optional<Member> findMemberByUserName(String userName);

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    //count query 에 대해서는 join 할 필요 없이 실행되도록 함
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.userName) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

//    Slice<Member> findByAge(int age, Pageable pageable);
}
