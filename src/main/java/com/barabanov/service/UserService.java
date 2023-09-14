package com.barabanov.service;


import com.barabanov.dao.UserRepository;
import com.barabanov.dto.UserCreateDTO;
import com.barabanov.dto.UserReadDto;
import com.barabanov.mapper.UserCreateMapper;
import com.barabanov.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
public class UserService
{

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;


    @Transactional
    public Integer create(UserCreateDTO userDto)
    {
        // validation dto
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var validationResult = validator.validate(userDto);
        if (!validationResult.isEmpty())
            throw new ConstraintViolationException(validationResult);

        var userEntity = userCreateMapper.mapFrom(userDto);
        return userRepository.save(userEntity).getId();
    }


    @Transactional
    public Optional<UserReadDto> findById(Integer id)
    {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(), userRepository.getEntityManager().getEntityGraph("withCompany")
        );
        return userRepository.findById(id, properties)
                .map(userReadMapper::mapFrom);
    }


    // boolean хотим по нашей бизнес логике.
    // Например, отправить был ли объект удалён или нет куда-то
    @Transactional
    public boolean delete(Integer id)
    {
        var mayBeUser = userRepository.findById(id);
        mayBeUser.ifPresent(user -> userRepository.delete(user.getId()));

        return mayBeUser.isPresent();
    }
}
