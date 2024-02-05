package com.RPA.request;

import com.RPA.entity.num.OperatingSystem;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на обновление скрипта")
public record UpdateScriptRequest(
        @Schema(description = "Код скритпа", example = "move 0.5 0.5")
        String code,

        @Schema(description = "Описание скрипта", example = "Перевод текста")
        String description,

        @Schema(description = "Входящие параметры", example = "[\"name1\" : {\"type\" : \"type1\", \"value\" : \"someValue\"}, ...]")
        JsonNode inputData,

        @Schema(description = "Версия скрипта", example = "0.1")
        String version,

        @Schema(description = "Операционная система", example = "Windows")
        OperatingSystem os
) {}
