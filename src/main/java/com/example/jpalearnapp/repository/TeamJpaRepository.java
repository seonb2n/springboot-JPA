package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {

    Optional<Team> findTeamByName(String name);
}
