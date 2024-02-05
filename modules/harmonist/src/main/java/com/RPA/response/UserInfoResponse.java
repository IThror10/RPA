package com.RPA.response;

import com.RPA.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Информация о пользователе")
public class UserInfoResponse {
    @Schema(description = "Имя пользователя", example = "Jone")
    String login;
    @Schema(description = "Имя пользователя", example = "Jon@gmail.com")
    String email;
    @Schema(description = "Имя пользователя", example = "8-2000")
    String phone;

    public UserInfoResponse(User user) {
        login = user.getUsername();
        email = user.getEmail();
        phone = user.getPhone();
    }
}
