package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member  m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public List<Member> findMemberByUsernameAndAgeGreaterThen(String userName, int age) {
        return em.createQuery("select m from Member m" +
                " where m.userName = :userName" +
                " and m.age > :age")
                .setParameter("userName", userName)
                .setParameter("age", age)
                .getResultList();
    }

    //이름으로 내림차순해서 페이징
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m" +
                " where m.age = :age order by m.userName desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    //나이로 개수 가져오기
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class).getSingleResult();
    }
}
