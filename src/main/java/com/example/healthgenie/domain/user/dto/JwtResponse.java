package com.example.healthgenie.domain.user.dto;

import com.example.healthgenie.domain.user.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {

    private Long userId;
    private Role role;
    private String accessToken;
    private String refreshToken;
}