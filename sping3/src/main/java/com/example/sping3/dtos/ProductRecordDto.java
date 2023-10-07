package com.example.sping3.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecordDto(@NotBlank(message = "Nome em branco") @NotNull(message = "Nome Vazio") String name,
                               @NotNull(message = "Valor nulo") BigDecimal value, String description) {

}
