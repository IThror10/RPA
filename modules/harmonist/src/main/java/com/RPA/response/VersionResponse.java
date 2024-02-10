package com.RPA.response;

import com.RPA.entity.Robot;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о версиях робота")
public class VersionResponse {
    @Schema(description = "Идентификатор версии", example = "12")
    Long id;

    @Schema(description = "Версия", example = "0.1")
    String version;

    @Schema(description = "Обратная совместимость", example = "0.0")
    String compatibleFrom;

    public VersionResponse(Robot robot) {
        id = robot.getId();
        version = robot.getVersion();
        compatibleFrom = robot.getVersion_from();
    }
}
