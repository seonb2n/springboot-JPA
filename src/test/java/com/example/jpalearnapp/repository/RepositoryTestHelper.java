package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.Team;

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
        createMember("aaa", 15);
        createMember("bbb", 20);
        createMember("ccc", 25);
        createMember("ddd", 19);
        createTeam("Team A");
        createTeam("Team B");

        memberjpaRepository.findByUserName("aaa").get().setTeam(teamJpaRepository.findTeamByName("Team A").get());
        memberjpaRepository.findByUserName("bbb").get().setTeam(teamJpaRepository.findTeamByName("Team A").get());
        memberjpaRepository.findByUserName("ccc").get().setTeam(teamJpaRepository.findTeamByName("Team B").get());
        memberjpaRepository.findByUserName("ddd").get().setTeam(teamJpaRepository.findTeamByName("Team B").get());
    }


}
