package com.RPA.controller;

import com.RPA.entity.Group;
import com.RPA.request.AuthorizeUserRequest;
import com.RPA.request.ChangeUserDataRequest;
import com.RPA.request.CreateGroupRequest;
import com.RPA.request.RegisterUserRequest;
import com.RPA.response.GroupResponse;
import com.RPA.response.UserInfoResponse;
import com.RPA.response.UserTokenResponse;
import com.RPA.service.AuthenticationService;
import com.RPA.service.GroupService;
import com.RPA.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final AuthenticationService authService;
    private final UserService userService;

    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully create",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserTokenResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Data already used",
                    content = @Content)
    })
    @PostMapping(value = "", produces = {"application/json"})
    public ResponseEntity<UserTokenResponse> registerUser(
            @RequestBody RegisterUserRequest request
    ) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @Operation(summary = "Authorize user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Create JWT token",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserTokenResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content)
    })
    @PostMapping(value = "/login", produces = {"application/json"})
    public ResponseEntity<UserTokenResponse> authorizeUser(
            @RequestBody AuthorizeUserRequest request
    ) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User information",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not Found",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/{userName}", produces = {"application/json"})
    public ResponseEntity<UserInfoResponse> findUser(
            @PathVariable String userName
    ) {
        return ResponseEntity.ok(new UserInfoResponse(userService.getByUsername(userName)));
    }

    @Operation(summary = "Find users by username")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User information",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not Found",
                    content = @Content
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "", produces = {"application/json"})
    public ResponseEntity<List<UserInfoResponse>> findUsersByTemplate(
            @RequestParam(name = "userName") String userName
    ) {
        return ResponseEntity.ok(userService.searchByUsername(userName));
    }

    @Operation(summary = "Update user info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated user information",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not Found",
                    content = @Content
            ),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping(value = "", produces = {"application/json"})
    public ResponseEntity<UserInfoResponse> updateUser(
            @RequestBody ChangeUserDataRequest request,
            @RequestAttribute("uid") Long userId
            ) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, request));
    }
}
