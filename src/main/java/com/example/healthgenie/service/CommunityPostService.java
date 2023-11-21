package com.example.healthgenie.service;


import com.example.healthgenie.domain.community.dto.PostRequest;
import com.example.healthgenie.domain.community.dto.PostResponse;
import com.example.healthgenie.domain.community.entity.CommunityPost;
import com.example.healthgenie.exception.CommunityPostException;
import com.example.healthgenie.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.healthgenie.exception.CommunityPostErrorResult.POST_EMPTY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    public static final int POST_COUNT = 20;
    private final CommunityPostRepository postRepository;

    public List<PostResponse> findAll() {
        return postRepository.findAll().stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent()))
                .collect(Collectors.toList());
    }

    public PostResponse findById(Long id) {
        CommunityPost post = postRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Transactional
    public PostResponse save(PostRequest dto) {
        CommunityPost savedPost = postRepository.save(CommunityPost.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build());

        return PostResponse.builder()
                .id(savedPost.getId())
                .build();
    }

    @Transactional
    public void delete(Long id) {
        Optional<CommunityPost> opPost = postRepository.findById(id);
        if(opPost.isEmpty()) {
            throw new CommunityPostException(POST_EMPTY);
        }

        postRepository.delete(opPost.get());
    }

    @Transactional
    public Long update(Long id, PostRequest request) {
        CommunityPost post = postRepository.findById(id)
                .orElseThrow(() -> new CommunityPostException(POST_EMPTY));

        if(request.getTitle() != null) {
            post.updateTitle(request.getTitle());
        }

        if(request.getContent() != null) {
            post.updateContent(request.getContent());
        }

        return id;
    }

    //게시물작성
    /*
    public CommunityPostResponseDto addPost(CommunityPostRequestDto dto, Long userId){
        userId =1L;

        CommunityPost saveResult = postRepository.save(buildPost(dto,userId));

        // getId()를 건네주는 이유는 CommunityPostResponseDto 에 user_id뿐 이기 때문이다
        return new CommunityPostResponseDto(saveResult.getId());
    }

    //게시물 1개 조회
    public CommunityPostGetResponseDto getPost(Long postId){
        Optional<CommunityPost> optionalResult = postRepository.findById(postId);

        // 찾은 게시물이 없을 수 있는 경우의 수들을 throw 처리
        if(optionalResult.isEmpty() || !optionalResult.isPresent() || optionalResult==null){
            throw new CommunityPostException(CommunityPostErrorResult.POST_EMPTY);
        }

        CommunityPost post = optionalResult.get();
        CommunityPostGetResponseDto result = CommunityPostGetResponseDto.builder()
                .id(post.getId())
                .body(post.getBody())
                .title(post.getTitle())
                .likeCount(post.getLikeCount())
                .pics(post.getPics())
                .commentList(post.getCommentList())
                .build();
        return result;
    }

    //게시물 리스트 조회
    public Page<CommunityPost> getPostsByPage(int page,int size) {
        if(page<0){
            throw new CommunityPostException(CommunityPostErrorResult.PAGE_EMPTY);
        }
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CommunityPost> result = postRepository.findAll(pageable);

        if(result.getContent().isEmpty()){
            throw new CommunityPostException(CommunityPostErrorResult.POST_EMPTY);
        }
        return result;
    }



    public CommunityPost buildPost(CommunityPostRequestDto dto,Long userId){
        return CommunityPost.builder()
                .body(dto.getBody())
                .likeCount(dto.getLikeCount())
                .pics(dto.getPics())
                .title(dto.getTitle())
                .user(User.builder().id(userId).build())
                .build();
    }

     */

}
