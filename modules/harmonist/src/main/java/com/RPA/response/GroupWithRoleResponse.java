package com.RPA.response;

import com.RPA.entity.num.GroupRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Группа пользователя")
public class GroupWithRoleResponse {
    @Schema(description = "Информация о группе", implementation = GroupResponse.class)
    GroupResponse groupResponse;
    @Schema(description = "Роль пользователя")
    GroupRole groupRole;

    public GroupWithRoleResponse(Long gid, String description, String name, GroupRole role) {
        groupResponse = new GroupResponse(gid, name, description);
        groupRole = role;
    }
}
