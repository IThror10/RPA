package com.RPA.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Информация об управляемых и доступных скриптах")
public class ScriptWithRoleResponse {
    @Schema(description = "Информация о скрипте", implementation = ScriptResponse.class)
    ScriptResponse scriptResponse;

    @Schema(description = "Флаг владельца", example = "False")
    boolean isCreator;
}
