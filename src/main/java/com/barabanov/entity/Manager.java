package com.barabanov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Manager extends User
{
    private String projectName;

    @Builder // так мы сможем в builder использовать не только поля из Manager, но и из класса User
    public Manager(Integer id, PersonalInfo personalInfo, String username, Role role, Company company, Profile profile, List<UserChat> userChats, String projectName) {
        super(id, personalInfo, username, role, company, profile, userChats);
        this.projectName = projectName;
    }
}
