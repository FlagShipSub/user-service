package com.example.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/login")
public class DummyController {
    @GetMapping(value = "/test")
    public ResponseEntity<String> testApplication() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }
}
