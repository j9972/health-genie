package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.JwtResponse;
import com.example.healthgenie.boundedContext.user.dto.TokenRequest;
import com.example.healthgenie.boundedContext.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<Result> redirect(
            @PathVariable("registrationId") String registrationId,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        log.info("----- AuthController.redirect -----");
        log.info("registrationId={}", registrationId);
        log.info("code={}", code);
        log.info("state={}", state);

        JwtResponse response = authService.redirect(
                TokenRequest.builder()
                        .registrationId(registrationId)
                        .code(code)
                        .state(state)
                        .build()
        );

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Result> refreshToken(HttpServletRequest request) {
        JwtResponse response = authService.refreshToken(request.getHeader("AccessToken"), request.getHeader("RefreshToken"));

        return ResponseEntity.ok(Result.of(response));
    }

    @PostMapping("/logout/oauth2/code/{registrationId}")
    public void logout(@PathVariable String registrationId,
                       HttpServletRequest request,
                       HttpServletResponse response
    ) {
        /*
        TODO : 카카오/구글과 함께 로그아웃 기능 구현
               현재는 자체 서비스 로그아웃만 구현됨
         */
        authService.logout(request, response);
    }
}