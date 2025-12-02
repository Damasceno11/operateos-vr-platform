package br.com.workdb.operateosbackend.core.usecase.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TelemetryInput(
        @NotNull String sessionId,
        @PositiveOrZero double speed,
        boolean collision
) {}
