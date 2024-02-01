package com.RPA.controller;

import com.RPA.request.AuthorizeUserRequest;
import com.RPA.request.RegisterUserRequest;
import com.RPA.response.UserTokenResponse;
import com.RPA.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final AuthenticationService authService;
    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found Terms",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserTokenResponse.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content)
    })
    @PostMapping(value = "", produces = {"application/json"})
    public ResponseEntity registerUser(
            @RequestBody RegisterUserRequest request
    ) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @Operation(summary = "Authorize user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found Terms",
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
    public ResponseEntity authorizeUser(
            @RequestBody AuthorizeUserRequest request
    ) {
        return ResponseEntity.ok(authService.signIn(request));
    }
}
