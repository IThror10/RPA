package com.RPA.controller;

import com.RPA.request.ChangeGroupDataRequest;
import com.RPA.request.CreateGroupRequest;
import com.RPA.request.UsernameRequest;
import com.RPA.response.GroupResponse;
import com.RPA.response.GroupWithRoleResponse;
import com.RPA.response.MemberWithRoleResponse;
import com.RPA.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/group")
public class GroupController {
    private final GroupService groupService;

    @Operation(summary = "Create Group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group successfully created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Group with that name already exists",
                    content = @Content
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "", produces = {"application/json"})
    public ResponseEntity<GroupResponse> createGroup(
            @RequestBody CreateGroupRequest request,
            @RequestAttribute("uid") Long userId
    ) {
        return ResponseEntity.ok(groupService.createGroup(userId, request));
    }

    @Operation(summary = "Update Group Info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group successfully created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupResponse.class))
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
                    description = "Group with that name already exists",
                    content = @Content
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "/{groupId}", produces = {"application/json"})
    public ResponseEntity<GroupResponse> updateGroupInfo(
            @RequestBody ChangeGroupDataRequest request,
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId
    ) {
        return ResponseEntity.ok(groupService.updateGroup(userId, groupId, request));
    }

    @Operation(summary = "Delete Group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Group deleted",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/{groupId}", produces = {"application/json"})
    public ResponseEntity deleteGroup(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId
    ) {
        groupService.deleteGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Returns User's Groups")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GroupWithRoleResponse.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "", produces = {"application/json"})
    public ResponseEntity getUserGroups(
            @RequestAttribute("uid") Long userId
    ) {
        return ResponseEntity.ok(groupService.getUsersGroups(userId));
    }

    @Operation(summary = "Returns Group's members")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MemberWithRoleResponse.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/{groupId}", produces = {"application/json"})
    public ResponseEntity getGroupsMembers(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId
    ) {
        return ResponseEntity.ok(groupService.getGroupsMembers(groupId, userId));
    }

    @Operation(summary = "Invite user to the group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User invited",
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
                    description = "User Or Group Not Found",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already invited",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{groupId}/invite", produces = {"application/json"})
    public ResponseEntity inviteUser(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId,
            @RequestBody UsernameRequest request
    ) {
        groupService.inviteUser(userId, groupId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Accept invitation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Invitation accepted",
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
                    description = "User Or Group Not Found",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{groupId}/accept", produces = {"application/json"})
    public ResponseEntity acceptInvitation(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId
    ) {
        groupService.acceptInvitation(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Decline invitation")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Invitation declined",
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
                    description = "User Or Group Not Found",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{groupId}/decline", produces = {"application/json"})
    public ResponseEntity declineInvitation(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId
    ) {
        groupService.declineInvitation(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Leave the group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Group left",
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
                    description = "User Or Group Not Found",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{groupId}/leave", produces = {"application/json"})
    public ResponseEntity leaveGroupEndPoint(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId
    ) {
        groupService.leaveGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove the member from group")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Member removed",
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
                    description = "User Or Group Not Found",
                    content = @Content),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{groupId}/remove", produces = {"application/json"})
    public ResponseEntity removeMemberEndPoint(
            @RequestAttribute("uid") Long userId,
            @PathVariable Long groupId,
            @RequestBody UsernameRequest request
    ) {
        groupService.excludeUser(userId, groupId, request);
        return ResponseEntity.noContent().build();
    }
}
