package com.barabanov.dto;

import com.barabanov.entity.PersonalInfo;
import com.barabanov.entity.Role;

public record UserReadDto(Integer id,
                          PersonalInfo personalInfo,
                          Role role,
                          CompanyReadDto company) { }
