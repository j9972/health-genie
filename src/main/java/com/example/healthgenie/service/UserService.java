package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.TestSignUpRequest;
import com.example.healthgenie.domain.user.dto.TestSignUpResponse;
import com.example.healthgenie.domain.user.dto.UserLoginResponseDto;
import com.example.healthgenie.domain.user.dto.UserRegisterDto;
import com.example.healthgenie.domain.user.entity.User;

public interface UserService {


    User signUp(UserRegisterDto userSignupRequestDto);
    UserLoginResponseDto socialLogin(String Email);
    UserLoginResponseDto addDummyUser(UserRegisterDto dto);
    TestSignUpResponse createUser(TestSignUpRequest signUpRequest);



//    public List<User> findMembers();
//    public Optional<User> findOne(Long userId);

}
