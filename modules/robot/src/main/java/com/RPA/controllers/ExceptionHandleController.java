package com.RPA.controllers;

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
    @Hidden
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleFormatExceptions(RuntimeException ex) {
        return createBody(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity createBody(String error, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", new Date().toString());
        map.put("status", status.toString());
        map.put("error", error);
        return new ResponseEntity(map, status);
    }
}
