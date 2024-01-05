package com.RPA.controllers;

import com.RPA.requests.CreateUserRequest;
import com.RPA.requests.HelloRequest;
import com.RPA.responses.HelloResponse;
import com.RPA.services.HelloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class HelloController {
    private final HelloService service;

    @Operation(summary = "Get Hello Message")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Hello message to User",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User Not Found",
            content = @Content
        )
    })
    @GetMapping(value = "/hello/{userName}", produces = {"application/json"})
    public ResponseEntity helloUserName(
        @RequestParam(name = "userStatus", required = false, defaultValue = "student") String status,
        @PathVariable String userName
    ) {
        HelloRequest request = new HelloRequest(userName, status);
        HelloResponse response = service.sayHello(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Accepted",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Wrong request",
                    content = @Content)})
    @PostMapping(value = "/hello", produces = { "application/json" }, consumes = { "application/json" })
    public ResponseEntity createUser(
            @RequestBody CreateUserRequest request
    ) {
        service.createUser(request);
        return ResponseEntity.accepted().build();
    }
}
