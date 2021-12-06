package com.example.jpalearnapp.entity.dto;

import com.example.jpalearnapp.entity.Member;
import com.example.jpalearnapp.entity.Team;
import lombok.Data;

@Data
public class MemberDto {

    private String userName;
    private int age;
    private String teamName;

    public MemberDto(String userName, int age, String teamName) {
        this.userName = userName;
        this.age = age;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        this.userName = member.getUserName();
        this.age = member.getAge();

    }
}
