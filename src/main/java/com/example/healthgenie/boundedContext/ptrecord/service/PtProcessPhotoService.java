package com.example.healthgenie.boundedContext.ptrecord.service;

import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import com.example.healthgenie.base.exception.PtProcessErrorResult;
import com.example.healthgenie.base.exception.PtProcessException;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessPhotoRepository;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PtProcessPhotoService {
    private final PtProcessPhotoRepository ptProcessPhotoRepository;
    private final PtProcessRepository ptProcessRepository;

    @Transactional
    public List<PtProcessPhoto> saveAll(Long processId, List<String> photoPaths) {
        PtProcess process = ptProcessRepository.findById(processId)
                .orElseThrow(() -> new PtProcessException(PtProcessErrorResult.NO_PROCESS_HISTORY));

        List<PtProcessPhoto> photos = photoPaths.stream()
                .map(path -> PtProcessPhoto.builder()
                        .processPhotoPath(path)
                        .process(process)
                        .build())
                .toList();

        // 객체 그래프 탐색용
        for (PtProcessPhoto photo : photos) {
            process.addProcess(photo);
        }

        return ptProcessPhotoRepository.saveAll(photos);
    }
}