package com.example.healthgenie.service;

import com.example.healthgenie.domain.user.dto.TestSignUpRequest;
import com.example.healthgenie.domain.user.dto.TestSignUpResponse;
import com.example.healthgenie.domain.user.dto.UserLoginResponseDto;
import com.example.healthgenie.domain.user.dto.UserRegisterDto;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.CommonErrorResult;
import com.example.healthgenie.exception.CommonException;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  
    private final UserRepository userRepository;
    @Transactional
    @Override
    public User signUp(UserRegisterDto dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .authProvider(dto.getAuthProvider())
                .uniName(dto.getUniName())
                .role(dto.getRole())
                .build();

        return userRepository.save(user);
    }


    @Transactional
    public TestSignUpResponse createUser(TestSignUpRequest signUpRequest){
        if(userRepository.existsByEmailAndAuthProvider(signUpRequest.getEmail(), signUpRequest.getAuthProvider())){
            throw new CommonException(CommonErrorResult.BAD_REQUEST);
        }

        User savedUser = userRepository.save(
                User.builder()
                        .name(signUpRequest.getNickname())
                        .email(signUpRequest.getEmail())
                        .role(signUpRequest.getRole())
                        .authProvider(signUpRequest.getAuthProvider())
                        .build()
        );

        return TestSignUpResponse.builder()
                .id(savedUser.getId())
                .authProvider(savedUser.getAuthProvider())
                .createdDate(savedUser.getCreatedDate())
                .nickname(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public UserLoginResponseDto socialLogin(String Email) {
        return null;
    }

    @Override
    public UserLoginResponseDto addDummyUser(UserRegisterDto dto) {
        return null;
    }
}
