package com.barabanov.mapper;

import com.barabanov.dto.UserReadDto;
import com.barabanov.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto>
{

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto mapFrom(User object) {
        return new UserReadDto(
                object.getId(),
                object.getPersonalInfo(),
                object.getRole(),
                // в случае, если company не NOT NULL в БД.
                Optional.ofNullable(object.getCompany())
                                .map(companyReadMapper::mapFrom)
                                        .orElse(null)
        );
    }
}
