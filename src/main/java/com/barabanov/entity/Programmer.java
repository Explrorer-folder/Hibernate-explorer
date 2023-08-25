package com.barabanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Programmer extends User
{

    @Builder // так мы сможем в builder использовать не только поля из Programmer, но и из класса User
    public Programmer(Integer id, PersonalInfo personalInfo, String username, Role role, Company company, Profile profile, List<UserChat> userChats, Language language) {
        super(id, personalInfo, username, role, company, profile, userChats);
        this.language = language;
    }

    @Enumerated(EnumType.STRING)
    private Language language;
}
