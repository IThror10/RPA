package com.RPA.request;

import com.RPA.entity.num.OperatingSystem;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о скрипте")
public class CodeRequest {
    @Schema(description = "Код скрипта", example = "move 0.5 0.5")
    private String code;

    @Schema(description = "Входные данные", example = "[{\"name1\" : \"X\", \"type\" : \"integer\", \"value\" : 0.5}, ...]")
    private JsonNode inputData;

    @Schema(description = "Версия языка скрипта", example = "0.1")
    private String version;

    @Schema(description = "Операционная система", example = "Windows")
    private OperatingSystem os;
}
