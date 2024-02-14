package com.RPA.controller;

import com.RPA.request.CreateScriptRequest;
import com.RPA.request.UpdateScriptRequest;
import com.RPA.request.UsernameRequest;
import com.RPA.response.GroupsAndAuthorsResponse;
import com.RPA.response.ScriptResponse;
import com.RPA.response.ScriptWithRoleResponse;
import com.RPA.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/script")
public class ScriptController {
    private final ScriptService service;

    @Operation(summary = "Create Script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Script successfully created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ScriptResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Script with that name already exists",
                    content = @Content
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "", produces = {"application/json"})
    public ResponseEntity<ScriptResponse> createScriptEp(
            @RequestBody CreateScriptRequest request,
            @RequestAttribute("uid") Long userId
    ) {
        return ResponseEntity.ok(service.createScript(userId, request));
    }

    @Operation(summary = "Update Script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Script successfully Updated",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ScriptResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Script with that name already exists",
                    content = @Content
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/{script_id}", produces = {"application/json"})
    public ResponseEntity<ScriptResponse> updateScriptEp(
            @RequestBody UpdateScriptRequest request,
            @RequestAttribute("uid") Long userId,
            @PathVariable Long script_id
    ) {
        return ResponseEntity.ok(service.updateScript(userId, script_id, request));
    }

    @Operation(summary = "Delete Script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Script successfully deleted",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Script not found",
                    content = @Content
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/{script_id}", produces = {"application/json"})
    public ResponseEntity<ScriptResponse> deleteScriptEp(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long script_id
    ) {
        service.deleteScript(userId, script_id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Allow group to execute script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Group successfully received script",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{scriptId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity grantScriptEp(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long scriptId,
            @PathVariable Long groupId
    ) {
        service.grantScriptToGroup(userId, scriptId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Revoke rights to execute script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Grant revoked",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/{scriptId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity revokeScriptEp(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long scriptId,
            @PathVariable Long groupId
    ) {
        service.revokeScriptFromGroup(userId, scriptId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Allow user to manage script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "User is added to authors",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{scriptId}/author", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAuthorEp(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long scriptId,
            @RequestBody UsernameRequest request
    ) {
        service.addAuthorToScript(userId, scriptId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Revoke rights from user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Rights revoked",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/{scriptId}/author/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeAuthorEp(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long scriptId,
            @PathVariable String username
    ) {
        service.removeAuthorFromScript(userId, scriptId, username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get managing scripts")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Scripts where user is author",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ScriptWithRoleResponse.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/manage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getManagingScriptsEp(
            @RequestAttribute("uid") Long userId
    ) {
        return ResponseEntity.ok(service.getManagingScripts(userId));
    }

    @Operation(summary = "Get executing scripts")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Scripts user can execute",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ScriptResponse.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExecutingScriptsEp(
            @RequestAttribute("uid") Long userId
    ) {
        return ResponseEntity.ok(service.getExecutingScripts(userId));
    }

    @Operation(summary = "Returns information about authors and executors of script")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authors and executors found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupsAndAuthorsResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/{scriptId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getInfoAboutScriptEp(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long scriptId
    ) {
        return ResponseEntity.ok(service.getScriptInfo(userId, scriptId));
    }
}
