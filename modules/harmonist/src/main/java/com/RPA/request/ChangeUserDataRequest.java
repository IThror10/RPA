package com.RPA.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Изменение публичной информации")
public class ChangeUserDataRequest {
    @Schema(description = "Адрес электронной почты", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @Null(message = "Адрес электронной может быть не указан")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    String email;

    @Schema(description = "Телефон", example = "8-800-555-35-35")
    @Size(min = 5, max = 255, message = "Формат телефон выбирается пользователем")
    @Null(message = "Телефон может быть не указан")
    String phone;
}
