package com.barabanov.dto;

import com.barabanov.entity.PersonalInfo;
import com.barabanov.entity.Role;
import com.barabanov.validation.UpdateCheck;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public record UserCreateDTO(
                            @Valid
                            PersonalInfo personalInfo,
                            @NotNull
                            String userName,
                            @NotNull(groups = UpdateCheck.class)
                            Role role,
//                            @ValidCompany могли бы написать кастомный валидатор на то, чтобы эта компания существовала
                            Integer companyId) {
}
