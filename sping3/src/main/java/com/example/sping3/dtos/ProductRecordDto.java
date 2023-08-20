package com.example.sping3.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDto(@NotBlank @NotNull String name, @NotNull BigDecimal value) {

}
