package com.example.healthgenie.controller;

import com.example.healthgenie.domain.user.dto.*;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.exception.CommonErrorResult;
import com.example.healthgenie.exception.CommonException;
import com.example.healthgenie.service.EmailService;
import com.example.healthgenie.service.KakaoService;
import com.example.healthgenie.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 DI
@RequestMapping("api/v1/auth")

public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final KakaoService kakaoService;

    // 회원가입
    @PostMapping("/signup") // http://localhost:1234/api/v1/auth/signup
    public ResponseEntity signUp(@RequestBody userRegisterDto request) {
        Long resultId = userService.signUp(request);
        return new ResponseEntity(resultId,HttpStatus.OK);
    }


    // 이메일 코드전송,이메일유효성검사
    @PostMapping("/mail/send") // http://localhost:1234/api/v1/auth/mail/send
    public String authMail(@RequestBody emailRequestDto request) {
        return userService.authMail(request.getEmail());
    }


    //이메일 코드검증
    @PostMapping("/mail/verify") // http://localhost:1234/api/v1/auth/mail/verify
    public ResponseEntity validMailCode(@RequestBody emailRequestDto request){
        String result = emailService.valiedCode(request.getCode());
        return new ResponseEntity(result,HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login") // http://localhost:1234/api/v1/auth/login
    public ResponseEntity<String> login(@RequestBody userLoginDto request) {
        return userService.login(request);
    }

    //소셜 로그인 카카오
    @PostMapping("/login/kakao") // http://localhost:1234/api/v1/auth/login/kakao
    public ResponseEntity signupByKakao(@RequestBody SocialSignupRequestDto socialSignupRequestDto) {
        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(socialSignupRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        return userService.socialLogin(kakaoProfile);
    }

    //소셜 회원가입 카카오
    @PostMapping("/signup/kakao") // http://localhost:1234/api/v1/auth/signup/kakao
    public ResponseEntity signupBySocial(@RequestBody SocialSignupRequestDto socialSignupRequestDto) {

        KakaoProfile kakaoProfile =
                kakaoService.getKakaoProfile(socialSignupRequestDto.getAccessToken());

        if (kakaoProfile == null) throw new CommonException(CommonErrorResult.ITEM_EMPTY);

        if (kakaoProfile.getKakao_account().getEmail() == null) {
            kakaoService.kakaoUnlink(socialSignupRequestDto.getAccessToken());
            throw new CommonException(CommonErrorResult.ITEM_EMPTY);
        }

        Long userId = userService.socialSignup(userRegisterDto.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .role(Role.USER)
                .name(kakaoProfile.getProperties().getNickname())
                .uniName(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());
        return new ResponseEntity(userId,HttpStatus.OK);
    }
}
