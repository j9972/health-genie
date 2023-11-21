package com.example.healthgenie.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {

    private Long id;
    private String title;
    private String content;
    private String photoName;
    private String photoPath;
}
