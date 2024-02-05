package com.RPA.response;

import com.RPA.entity.Group;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupResponse {
    @Schema(description = "Идентификатор группы", example = "5")
    private Long id;

    @Schema(description = "Имя группы", example = "ITMO students")
    private String name;

    @Schema(description = "Описание группы", example = "The bravest of brave")
    private String description;

    public GroupResponse(Group group) {
        id = group.getId();
        name = group.getName();
        description = group.getDescription();
    }
}
