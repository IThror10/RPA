package com.RPA.request;

import com.RPA.entity.num.OperatingSystem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Запрос с информацией от робота")
public class RobotInfoRequest {
    @Schema(description = "Операционная система", example = "Windows")
    @NotBlank(message = "Должны быть указана")
    OperatingSystem operatingSystem;

    @Schema(description = "Хост и порт робота", example = "someHost:80")
    @NotBlank(message = "Должен быть передан")
    String hostPort;

    @Schema(description = "Ключ аутентификации робота", example = "SecretKey")
    @NotBlank(message = "Ключ должен быть передан")
    String secret;

    @Schema(description = "Версия робота", example = "0.1")
    @NotBlank(message = "Версия должны быть указана")
    String version;

    @Schema(description = "Обратная совместимость", example = "0.0")
    @NotBlank(message = "Версия должны быть указана")
    String versionFrom;

    @Schema(description = "Мануал", example = "Some manual")
    @NotBlank(message = "Мануал должен быть передан")
    String man;
    @Schema(description = "Группы с доступом к роботу", example = "[1, 2, 3]")
    List<Long> groups;

    @Schema(description = "Доступен для интеркативного взаимодействия", example = "False")
    Boolean interactive;
}
