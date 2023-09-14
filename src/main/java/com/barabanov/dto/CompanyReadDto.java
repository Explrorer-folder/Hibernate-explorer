package com.barabanov.dto;

import java.util.List;

public record CompanyReadDto(Integer id,
                             String name,
                             List<String> locales) {
}
