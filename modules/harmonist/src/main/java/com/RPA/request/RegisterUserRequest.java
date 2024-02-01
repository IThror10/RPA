package com.RPA.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию")
public record RegisterUserRequest(
        @Schema(description = "Имя пользователя", example = "Jon")
        @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
        @NotBlank(message = "Имя пользователя не может быть пустыми")
        String login,

        @Schema(description = "Пароль", example = "my_1secret1_password")
        @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
        String password,

        @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
        @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
        @NotBlank(message = "Адрес электронной почты не может быть пустыми")
        @Email(message = "Email адрес должен быть в формате user@example.com")
        String email,

        @Schema(description = "Телефон", example = "8-800-555-35-35")
        @Size(min = 5, max = 255, message = "Формат телефон выбирается пользователем")
        @Null(message = "Телефон может быть не указан")
        String phone
) {}
