package com.RPA.response;

import com.RPA.entity.Script;
import com.RPA.entity.num.OperatingSystem;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о скрипте")
public class ScriptResponse {
    @Schema(description = "Идентификатор скрипта", example = "1")
    private Long id;
    @Schema(description = "Код скрипта", example = "move 0.5 0.5")
    private String code;
    @Schema(description = "Название скрипта", example = "Mouse Move")
    private String name;
    @Schema(description = "Описание скрипта", example = "Переместить мышь в центр экрана")
    private String description;
    @Schema(description = "Входящие данные", example = "[\"name1\" : {\"type\" : \"type1\", \"value\" : \"someValue\"}, ...]")
    private JsonNode inputData;
    @Schema(description = "Версия кода скрипта", example = "0.1")
    private String version;
    @Schema(description = "Операционная система", example = "Windows")
    private OperatingSystem os;

    public ScriptResponse(Script script) {
        id = script.getId();
        code = script.getCode();
        name = script.getName();
        description = script.getDescription();
        inputData = script.getInputData();
        version = script.getVersion();
        os = script.getOs();
    }
}
