package com.RPA.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Data;


@Data
@Schema(description = "Запрос на изменение описания группы")
public class ChangeGroupDataRequest {
    @Schema(description = "Название группы", example = "ITMO Students")
    @Null(message = "Название может быть не задано")
    private String name;

    @Schema(description = "Описание", example = "Храбрейшие из храбрейших")
    @Null(message = "Описание может быть не задано")
    String description;
}
