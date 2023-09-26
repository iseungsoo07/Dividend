package com.zerobase.mydividend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<?> signUp() {
        return null;
    }

    @PostMapping("/singIn")
    public ResponseEntity<?> signIn() {
        return null;
    }
}
