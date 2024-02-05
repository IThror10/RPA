package com.RPA.controller;

import com.RPA.exception.ConflictException;
import com.RPA.exception.ForbiddenException;
import com.RPA.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class ExceptionHandleController {
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity handleConflictException(ConflictException ex) {
        return createBody(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNotFoundException(NotFoundException ex) {
        return createBody(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handleRuntimeException(ForbiddenException ex) {
        return createBody(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return createBody("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity createBody(String error, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", new Date().toString());
        map.put("status", status.toString());
        map.put("error", error);
        return new ResponseEntity(map, status);
    }
}
