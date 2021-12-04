package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class RepositoryTestHelper {

    private MemberJpaRepository memberjpaRepository;
    private TeamJpaRepository teamJpaRepository;

    public RepositoryTestHelper(MemberJpaRepository memberjpaRepository, TeamJpaRepository teamJpaRepository) {
        this.memberjpaRepository = memberjpaRepository;
        this.teamJpaRepository = teamJpaRepository;
    }

    public Member createMember(String name, int age) {
        return memberjpaRepository.save(new Member(name, age));
    }

    public Team createTeam(String name) {
        return teamJpaRepository.save(new Team(name));
    }

    public void initDB() {
        createMember("aaa", 20);
        createMember("bbb", 15);
        createMember("ccc", 13);
        createMember("ddd", 21);
        createTeam("Team A");
        createTeam("Team B");

        memberjpaRepository.findMemberByUserName("aaa").get().setTeam(teamJpaRepository.findTeamByName("Team A").get());
        memberjpaRepository.findMemberByUserName("bbb").get().setTeam(teamJpaRepository.findTeamByName("Team A").get());
        memberjpaRepository.findMemberByUserName("ccc").get().setTeam(teamJpaRepository.findTeamByName("Team B").get());
        memberjpaRepository.findMemberByUserName("ddd").get().setTeam(teamJpaRepository.findTeamByName("Team B").get());
    }


}
