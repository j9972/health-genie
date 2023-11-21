package com.example.healthgenie.controller;

import com.example.healthgenie.domain.community.dto.PostRequest;
import com.example.healthgenie.domain.community.dto.PostResponse;
import com.example.healthgenie.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/community/post")
public class CommunityPostController {

    private final CommunityPostService postService;

//    @PreAuthorize("isAnonymous()") // 로그인 안했을 경우에만 해당 URL 접속 가능
//    @PreAuthorize("isAuthenticated()") // 로그인 했을 경우에만 해당 URL 접속 가능
    @GetMapping
    public PostResponse findById(@RequestBody PostRequest request) {
        return postService.findById(request.getId());
    }

//    @PreAuthorize("permitAll()") // 아무나 해당 URL 접속 가능
    @PostMapping("/write")
    public PostResponse save(@RequestBody PostRequest request) {
        return postService.save(request);
    }

    //    @PreAuthorize("permitAll()") // 아무나 해당 URL 접속 가능
    @PatchMapping("/edit/{id}")
    public Long edit(@PathVariable Long id, @RequestBody PostRequest request) {
        return postService.update(id, request);
    }

//    @PreAuthorize("isAnonymous()") // 로그인 안했을 경우에만 해당 URL 접속 가능
    @DeleteMapping("/delete/{id}")
    public Long delete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }

/*
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

 */
}
