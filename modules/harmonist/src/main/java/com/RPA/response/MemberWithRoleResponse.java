package com.RPA.response;

import com.RPA.entity.num.GroupRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Пользователи группы")
public class MemberWithRoleResponse {
    @Schema(description = "Информация о пользователе", implementation = UserInfoResponse.class)
    UserInfoResponse userInfoResponse;
    @Schema(description = "Роль пользователя", example = "LEADER")
    GroupRole groupRole;

    public MemberWithRoleResponse(String username, String phone, String email, GroupRole role) {
        userInfoResponse = new UserInfoResponse(username, email, phone);
        groupRole = role;
    }
}