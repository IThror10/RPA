package com.RPA.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

@Schema(description = "Запрос на создание группы")
public record CreateGroupRequest(
                @Schema(description = "Имя группы", example = "ITMO students")
                @NotBlank(message = "Название группы не может быть пустыми")
                String name,

                @Schema(description = "Описание", example = "Храбрейшие из храбрейших")
                @Null(message = "Описание может быть не задано")
                String description
) {}
