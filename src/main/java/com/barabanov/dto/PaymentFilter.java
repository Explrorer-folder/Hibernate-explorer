package com.barabanov.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
// тут всё поля, которые будут использоваться для фильтрации.
public class PaymentFilter
{
    String firstName;
    String lastName;
}
