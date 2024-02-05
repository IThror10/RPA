package com.RPA.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Авторы и пользователи скрипта")
public class GroupsAndAuthorsResponse {
    @ArraySchema(schema = @Schema(implementation = GroupResponse.class))
    List<GroupResponse> executors;
    @ArraySchema(schema = @Schema(implementation = UserInfoResponse.class))
    List<UserInfoResponse> authors;
}
