package com.example.healthgenie.boundedContext.community.photo.service;

import com.example.healthgenie.base.exception.CommunityPostException;
import com.example.healthgenie.base.utils.S3UploadUtils;
import com.example.healthgenie.boundedContext.community.photo.dto.PhotoRequest;
import com.example.healthgenie.boundedContext.community.photo.entity.Photo;
import com.example.healthgenie.boundedContext.community.photo.repository.PhotoRepository;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.healthgenie.base.exception.CommunityPostErrorResult.PHOTO_EMPTY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final PostService postService;
    private final S3UploadUtils s3UploadUtils;

    @Transactional
    public List<Photo> save(Long postId, PhotoRequest request) throws IOException {
        List<Photo> photos = new ArrayList<>();

        Post post = postService.findById(postId);

        for(MultipartFile file : request.getPhotos()) {
            String uploadUrl = s3UploadUtils.upload(file, "post-photos");
            String originName = file.getOriginalFilename();

            Photo savedPhoto = photoRepository.save(
                    Photo.builder()
                            .path(uploadUrl)
                            .name(originName)
                            .post(post)
                            .build()
            );

            photos.add(savedPhoto);

//            post.addPhoto(savedPhoto);
        }

        return photos;
    }

    public Photo findById(Long photoId) {
        return photoRepository.findById(photoId)
                .orElseThrow(() -> new CommunityPostException(PHOTO_EMPTY));
    }

    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    public List<Photo> findAllByPostId(Long postId) {
        return photoRepository.findAllByPostId(postId);
    }

    @Transactional
    public String deleteAllByPostId(Long postId) {
        List<Photo> photos = findAllByPostId(postId);

        photoRepository.deleteAllByPostId(postId);

        return photos.size() + "개의 사진이 삭제 되었습니다.";
    }
}
