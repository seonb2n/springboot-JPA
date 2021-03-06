package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    List<Member> findByUserNameAndAgeGreaterThan(String username, int age);

    @Query("select new com.example.jpalearnapp.entity.dto.MemberDto(m.userName, m.age, t.name) " +
            "from Member m " +
            "join m.team t")
    List<MemberDto> findMemberDto();

    Optional<Member> findByUserName(String userName);

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    //count query 에 대해서는 join 할 필요 없이 실행되도록 함
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.userName) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

//    Slice<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //update 쿼리를 사용하기 위해서는 @Modifying 필요
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberUsingFetchJoin();

    //entity graph 사용해서 fetch join 해결하기
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Member findLockByUserName(String username);

    List<UsernameOnly> findProjectionsByUserName(@Param("userName") String userName);

    List<UserNameOnlyDto> findProjections2ByUserName(@Param("userName") String userName);

    @Query(value = "select m.member_id as id, m.userName, t.name as teamName "
                    + "from member m left join team t",
                    countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProection(Pageable pageable);
}
