package com.example.healthgenie.boundedContext.trainer.photo.repository;

import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerProfilePhotoRepository extends JpaRepository<TrainerPhoto, Long> {

    void deleteAllByInfoId(Long profileId);

    void deleteByInfoId(Long infoId);
}
