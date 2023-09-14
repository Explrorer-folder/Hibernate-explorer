package com.barabanov.mapper;

import com.barabanov.dao.CompanyRepository;
import com.barabanov.dto.UserCreateDTO;
import com.barabanov.entity.User;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDTO, User>
{

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDTO object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.userName())
                .role(object.role())
                // Можно кидать исключение, если там не может не быть company.
                // Это должно проверяться на уровне валидации, так что уровень map должен быть без проблем
                // и на этом этапе не должно произойти этого исключения
                .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
