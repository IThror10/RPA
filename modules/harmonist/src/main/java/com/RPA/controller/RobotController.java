package com.RPA.controller;

import com.RPA.entity.InMemoryRobot;
import com.RPA.request.CodeRequest;
import com.RPA.request.RobotInfoRequest;
import com.RPA.response.RobotResponse;
import com.RPA.response.VersionResponse;
import com.RPA.service.GroupService;
import com.RPA.service.RobotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/robot")
public class RobotController {
    private final RobotService robotService;
    private final GroupService groupService;

    @Operation(summary = "Register Robot")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Register Request Accepted",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content)
    })
    @PostMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity registerRobot(
            @RequestBody RobotInfoRequest request
            ) {
        robotService.registerRobot(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Request accepted for processing");
    }

    @Operation(summary = "Unregister Robot")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Robots",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content)
    })
    @DeleteMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity unregisterRobot(
            @RequestBody RobotInfoRequest request
    ) {
        robotService.unregisterRobot(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Request accepted for processing");
    }
    @Operation(summary = "Get info about available robots")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Robots",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RobotResponse.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity availableRobots(
            @RequestAttribute("uid") Long userId
    ) {
        return ResponseEntity.ok(robotService.getInteractiveRobots(userId));
    }

    @Operation(summary = "Get info about existing versions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found Versions",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = VersionResponse.class)))
                    })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity currentVersions() {
        return ResponseEntity.ok(robotService.getExistingVersions());
    }

    @Operation(summary = "Get version manual")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manual",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/version/{versionId}/man", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity manualEp(
            @PathVariable Long versionId
    ) {
        return ResponseEntity.ok(robotService.getManual(versionId));
    }

    @Operation(summary = "Check the script on errors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "No errors found",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/compile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity compileEp(
            @RequestBody CodeRequest request,
            @RequestAttribute("uid") Long userId
            ) {
        Set<Long> groups = groupService.getUserMembership(userId);
        InMemoryRobot robot = robotService.receiveRobot(groups, request.getVersion(), request.getOs());

        ResponseEntity response = robotService.compileScript(robot, request);
        robotService.releaseRobot(robot);
        return response;
    }

    @Operation(summary = "Execute script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Script successfully executed",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity executeEp(
            @RequestBody CodeRequest request,
            @RequestAttribute("uid") Long userId
    ) {
        Set<Long> groups = groupService.getUserMembership(userId);
        InMemoryRobot robot = robotService.receiveRobot(groups, request.getVersion(), request.getOs());

        ResponseEntity response = robotService.executeScript(robot, request);
        robotService.clearState(robot);
        robotService.releaseRobot(robot);
        return response;
    }
}