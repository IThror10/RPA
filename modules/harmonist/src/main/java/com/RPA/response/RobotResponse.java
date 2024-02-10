package com.RPA.response;

import com.RPA.entity.InMemoryRobot;
import com.RPA.entity.num.OperatingSystem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Информация о роботе")
public class RobotResponse {
    @Schema(description = "Operating System", example = "Windows")
    OperatingSystem os;
    @Schema(description = "Version of robot", example = "0.1")
    String version;

    public RobotResponse(InMemoryRobot robot) {
        os = robot.os();
        version = robot.version();
    }
}
