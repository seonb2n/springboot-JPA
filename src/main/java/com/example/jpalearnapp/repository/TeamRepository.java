package com.example.jpalearnapp.repository;

import com.example.jpalearnapp.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TeamRepository {

    @PersistenceContext
    private EntityManager em;

}
