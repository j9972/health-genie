package com.example.healthgenie.controller;

import com.example.healthgenie.domain.user.dto.JwtResponse;
import com.example.healthgenie.domain.user.dto.SignInResponse;
import com.example.healthgenie.domain.user.dto.TokenRequest;
import com.example.healthgenie.service.AuthService;
import com.example.healthgenie.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/login/oauth2/code/{registrationId}")
    public ResponseEntity<JwtResponse> redirect(
            @PathVariable("registrationId") String registrationId,
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        log.info("----- AuthController.redirect -----");
        log.info("registrationId={}", registrationId);
        log.info("code={}", code);
        log.info("state={}", state);

        SignInResponse result = authService.redirect(
                TokenRequest.builder()
                        .registrationId(registrationId)
                        .code(code)
                        .state(state)
                        .build()
        );

        JwtResponse jwt = JwtResponse.builder()
                .userId(result.getUserId())
                .role(result.getRole())
                .accessToken(result.getAccessToken())
                .refreshToken(result.getRefreshToken())
                .build();

        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/refresh")
    public ResponseEntity<SignInResponse> refreshToken(@RequestBody TokenRequest tokenRequest){
        log.info("----- AuthController refreshToken -----");
        log.info("TokenRequest.registrationId={}", tokenRequest.getRegistrationId());
        log.info("TokenRequest.refreshToken={}", tokenRequest.getRefreshToken());

        return ResponseEntity.ok(authService.refreshToken(tokenRequest));
    }

}