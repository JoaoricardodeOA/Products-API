package com.example.sping3.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WarehouseRecordDto(@NotBlank(message = "nome em branco") @NotNull(message = "nome vazio") String WarehouseName) {
}
