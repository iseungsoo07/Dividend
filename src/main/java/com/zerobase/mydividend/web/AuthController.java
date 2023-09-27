package com.zerobase.mydividend.web;

import com.zerobase.mydividend.model.Auth;
import com.zerobase.mydividend.persist.entity.MemberEntity;
import com.zerobase.mydividend.security.TokenProvider;
import com.zerobase.mydividend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Auth.SignUp request) {
        MemberEntity member = memberService.register(request);
        return ResponseEntity.ok(member);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Auth.SignIn request) {
        MemberEntity memberEntity = memberService.authenticate(request);
        String token = tokenProvider.generateToken(memberEntity.getUsername(), memberEntity.getRoles());
        log.info("{} 사용자 로그인 성공", request.getUsername());
        return ResponseEntity.ok(token);
    }
}
