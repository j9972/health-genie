package com.example.healthgenie.boundedContext.trainer.photo.service;

import com.example.healthgenie.base.exception.CustomException;
import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.photo.repository.TrainerProfilePhotoRepository;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.profile.repository.TrainerProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrainerProfilePhotoService {

    private final TrainerProfileRepository trainerProfileRepository;
    private final TrainerProfilePhotoRepository trainerProfilePhotoRepository;

    @Transactional
    public TrainerPhoto save(Long profileId, String path) {
        TrainerInfo profile = trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);

        TrainerPhoto photo = TrainerPhoto.builder()
                .infoPhotoPath(path)
                .info(profile)
                .build();

        return trainerProfilePhotoRepository.save(photo);
    }

    @Transactional
    public List<TrainerPhoto> saveAll(Long profileId, List<String> photoPaths) {
        TrainerInfo profile = trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);

        List<TrainerPhoto> photos = photoPaths.stream()
                .map(path -> TrainerPhoto.builder()
                        .infoPhotoPath(path)
                        .info(profile)
                        .build())
                .toList();

        // 객체 그래프 탐색용
        for (TrainerPhoto photo : photos) {
            profile.addPhoto(photo);
        }

        return trainerProfilePhotoRepository.saveAll(photos);
    }

    @Transactional
    public List<TrainerPhoto> updateAll(Long profileId, List<String> photoPaths) {
        TrainerInfo info = trainerProfileRepository.findById(profileId)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);

        // 객체 그래프 탐색용
        info.removePhotos(info.getTrainerPhotos());

        // 기존 Photo 삭제
        trainerProfilePhotoRepository.deleteAllByInfoId(profileId);

        // 새로운 Photo 저장
        return saveAll(profileId, photoPaths);
    }

    @Transactional(readOnly = true)
    public TrainerPhoto findById(Long id) {
        return trainerProfilePhotoRepository.findById(id)
                .orElseThrow(() -> CustomException.TRAINER_INFO_EMPTY);
    }

    @Transactional(readOnly = true)
    public List<TrainerPhoto> findAll() {
        return trainerProfilePhotoRepository.findAll();
    }
}