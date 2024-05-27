package com.hei.restauman.repository.model;

import com.hei.restauman.entity.enums.MovementType;

import java.time.Instant;

public record MovementDetails(
        Instant movementDatetime,
        String ingredientTemplateName,
        MovementType movementType,
        double quantity,
        String unit)
{}
