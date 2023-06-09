package com.example.healthgenie.controller;

import com.example.healthgenie.domain.community.dto.CommunityPostResponseDto;
import com.example.healthgenie.domain.community.dto.CommunityPostGetResponseDto;
import com.example.healthgenie.domain.community.dto.CommunityPostIdTitleDto;
import com.example.healthgenie.domain.community.dto.CommunityPostRequestDto;
import com.example.healthgenie.domain.community.dto.CommunityPostResponseDto;
import com.example.healthgenie.domain.community.entity.CommunityPost;
import com.example.healthgenie.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/community/post")
public class CommunityPostController {

    private final CommunityPostService postService;

    @PostMapping("/add")  // http://localhost:1234/api/v1/community/post/add
    public ResponseEntity addPost(@RequestBody CommunityPostRequestDto dto){

        Long userId =1L; // 로그인기능 완성전 임시 변수
        CommunityPostResponseDto result = postService.addPost(dto,userId);
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/get") // http://localhost:1234/api/v1/community/post/get
    public ResponseEntity getPost(@RequestParam Long postId){
        CommunityPostGetResponseDto result = postService.getPost(postId);

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/") // http://localhost:1234/api/v1/community/post/
    public ResponseEntity<Page<CommunityPostIdTitleDto>> getPostsByPage(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "20") int size) {

        Page<CommunityPost> posts = postService.getPostsByPage(page,size);
        Page<CommunityPostIdTitleDto> postDtos = posts.map(post -> new CommunityPostIdTitleDto(post.getId(), post.getTitle()));
        return new ResponseEntity(postDtos,HttpStatus.OK);
    }
}
