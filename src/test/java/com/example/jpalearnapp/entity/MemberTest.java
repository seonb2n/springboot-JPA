package com.example.jpalearnapp.entity;

import com.example.jpalearnapp.repository.MemberJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testEntity() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 15, teamA);
        Member member3 = new Member("member3", 20, teamB);
        Member member4 = new Member("member4", 12, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println(" >>> member = "+member);
            System.out.println("member.team = "+member.getTeam());
        }
    }

    @Test
    public void JpaEventBasaeEntity() throws Exception {
        Member member = new Member("member1");
        memberJpaRepository.save(member);

        Thread.sleep(100);

        member.setUserName("member2");

        em.flush(); //@PreUpdate
        em.clear();

        Member member1 = memberJpaRepository.findById(member.getId()).get();

        System.out.println("member1.getCreatedDate() = " + member1.getCreatedDate());
        System.out.println("member1.getUpdatedDate() = " + member1.getLastModifiedDate());
        System.out.println("member1.getCreatedBy() = " + member1.getCreatedBy());
        System.out.println("member1.getUpdatedBy() = " + member1.getLastModifiedBy());
    }
}