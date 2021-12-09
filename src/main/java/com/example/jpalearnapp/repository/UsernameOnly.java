package com.example.jpalearnapp.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    @Value("#{target.userName + ' ' + target.age}") //entity 를 가져오고, 원하는 데이터를 가공. openProjection
    String getUserName();
}
