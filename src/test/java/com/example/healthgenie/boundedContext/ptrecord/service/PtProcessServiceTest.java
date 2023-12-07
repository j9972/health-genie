package com.example.healthgenie.boundedContext.ptrecord.service;

import com.example.healthgenie.base.exception.*;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessResponseDto;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.ptreview.service.PtReviewService;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.util.TestSyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PtProcessServiceTest {

    @Autowired
    TestSyUtils testSyUtils;

    @Autowired
    PtProcessService processService;

    User user;
    User user2;
    User user3;
    User user4;
    PtProcess process;
    Matching matching;

    @BeforeEach
    void before() {
        LocalDateTime date = LocalDateTime.of(2023, 12, 5, 14, 30, 0);
        LocalDate date2 = LocalDate.of(2023,12,5);

        user = testSyUtils.createUser("test1", Role.USER,"jh485200@gmail.com");
        user2 = testSyUtils.createUser("test2", Role.TRAINER,"test@test.com");
        user3 = testSyUtils.createUser("test3", Role.USER,"test3@gmail.com");
        user4 = testSyUtils.createUser("test4", Role.TRAINER,"test4@test.com");

        matching = testSyUtils.createMatching(date, "gym", true, 20000, "test", user, user2);
        process = testSyUtils.createProcess(date2,"test title", "test content", user, user2);
    }

    @Test
    @DisplayName("트레이너가 피드백 생성 성공")
    void addPtProcess() {
        // given
        testSyUtils.login(user2);

        LocalDate date = LocalDate.of(2023,12,5);

        PtProcessRequestDto dto = testSyUtils.createProcessDto(date, "test title", "test content", "test1","test2");

        // when
        PtProcessResponseDto response = processService.addPtProcess(dto);

        // then
        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getContent()).isEqualTo("test content");
        assertThat(response.getTitle()).isEqualTo("test title");
        assertThat(response.getUserNickName()).isEqualTo("test1");
        assertThat(response.getTrainerNickName()).isEqualTo("test2");
    }

    @Test
    @DisplayName("회원이 피드백 생성 실패")
    void failUserAddPtProcess() {
        // given
        testSyUtils.login(user);

        LocalDate date = LocalDate.of(2023,12,5);

        PtProcessRequestDto dto = testSyUtils.createProcessDto(date, "test title", "test content", "test1","test2");

        // when

        // then
        assertThatThrownBy(() -> {
            if(!user.getRole().equals(Role.TRAINER)) {
                throw new PtProcessException(PtProcessErrorResult.NO_USER_INFO);
            }
        }).isInstanceOf(PtProcessException.class);
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 피드백 생성 실패")
    void noLoginAddPtProcess() {
        // given

        LocalDate date = LocalDate.of(2023,12,5);

        PtProcessRequestDto dto = testSyUtils.createProcessDto(date, "test title", "test content", "test1","test2");

        // when

        // then
        assertThatThrownBy(() -> processService.addPtProcess(dto))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("매칭 기록없이 리뷰 작성 실패")
    void noMatchingHistoryAddPtProcess() {
        // given
        testSyUtils.login(user3);
        LocalDate date = LocalDate.of(2023,12,5);

        PtProcessRequestDto dto = testSyUtils.createProcessDto(date, "test title", "test content", null ,"test3","test4");
        // when

        // then
        assertThatThrownBy(() -> processService.addPtProcess(dto))
                .isInstanceOf(MatchingException.class);
    }

    @Test
    @DisplayName("피드백 상세 조회하기")
    void getPtProcess() {
        // given
        testSyUtils.login(user);
        LocalDate date = LocalDate.of(2023,12,5);

        // when
        PtProcessResponseDto response = processService.getPtProcess(process.getId());

        // then
        assertThat(response.getDate()).isEqualTo(date);
        assertThat(response.getContent()).isEqualTo("test content");
        assertThat(response.getTitle()).isEqualTo("test title");
        assertThat(response.getUserNickName()).isEqualTo("test1");
        assertThat(response.getTrainerNickName()).isEqualTo("test2");

    }

    @Test
    @DisplayName("트레이너나 회원 외의 다른 사람이 피드백 상세 조회 실패하기")
    void failGetPtProcess() {
        // given
        testSyUtils.login(user3);

        // when

        // then
        assertThatThrownBy(() -> {
            if(!user3.getNickname().equals(process.getMember().getNickname())
                & !user3.getNickname().equals(process.getTrainer().getNickname())
            ) {
                throw new PtProcessException(PtProcessErrorResult.NO_USER_INFO);
            } else {
                processService.getPtProcess(process.getId());
            }
        }).isInstanceOf(PtProcessException.class);
    }

    @Test
    @DisplayName("로그인 안한 사람이 피드백 상세 조회 실패하기")
    void notLoginGetPtProcess() {
        // given

        // when

        // then
        assertThatThrownBy(() -> processService.getPtProcess(process.getId()))
                .isInstanceOf(PtProcessException.class);
    }

    @Test
    @DisplayName("존재하지 않는 피드백 상세 조회 실패하기")
    void notExistGetPtProcess() {
        // given
        testSyUtils.login(user);

        // when

        // then
        assertThatThrownBy(() -> processService.getPtProcess(999L))
                .isInstanceOf(PtProcessException.class);
    }

    @Test
    @DisplayName("트레이너가 작성한 본인의 모든 피드백 조회하기")
    void getAllTrainerProcess() {
        // given
        testSyUtils.login(user2);

        // when
        Page<PtProcessResponseDto> response = processService.getAllTrainerProcess(0, 5);
        LocalDate date = LocalDate.of(2023,12,5);

        // then
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getDate()).isEqualTo(date);
        assertThat(response.getContent().get(0).getContent()).isEqualTo("test content");
        assertThat(response.getContent().get(0).getTitle()).isEqualTo("test title");
        assertThat(response.getContent().get(0).getUserNickName()).isEqualTo("test1");
        assertThat(response.getContent().get(0).getTrainerNickName()).isEqualTo("test2");
        assertThat(response.getContent().get(0).getTrainerNickName()).isEqualTo(user2.getNickname());
    }

    @Test
    @DisplayName("나의 모든 피드백 조회하기")
    void getAllMyProcess() {
        // given
        testSyUtils.login(user);

        // when
        Page<PtProcessResponseDto> response = processService.getAllMyProcess(0, 5);
        LocalDate date = LocalDate.of(2023,12,5);

        // then
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getDate()).isEqualTo(date);
        assertThat(response.getContent().get(0).getContent()).isEqualTo("test content");
        assertThat(response.getContent().get(0).getTitle()).isEqualTo("test title");
        assertThat(response.getContent().get(0).getUserNickName()).isEqualTo("test1");
        assertThat(response.getContent().get(0).getTrainerNickName()).isEqualTo("test2");
        assertThat(response.getContent().get(0).getUserNickName()).isEqualTo(user.getNickname());
    }

    @Test
    @DisplayName("트레이너가 피드백 삭제하기")
    void deletePtProcess() {
        // given
        testSyUtils.login(user2);

        // when

        // then
        String response = processService.deletePtProcess(process.getId());
        assertThat(response).isEqualTo("피드백이 삭제 되었습니다.");
    }

    @Test
    @DisplayName("회원이 피드백 삭제 실패하기")
    void failUserDeletePtProcess() {
        // given
        testSyUtils.login(user);

        // when

        // then
        assertThatThrownBy(() -> {
            if(!user.getRole().equals(Role.TRAINER)) {
                throw new PtProcessException(PtProcessErrorResult.NO_USER_INFO);
            }
        }).isInstanceOf(PtProcessException.class);
    }

    @Test
    @DisplayName("로그인 하지 않은 유저가 피드백 삭제 실패하기")
    void notLoginDeletePtProcess() {
        // given

        // when

        // then
        assertThatThrownBy(() -> processService.deletePtProcess(process.getId()))
                .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("키워드로 조회하기")
    void findAll() {
// given
        testSyUtils.login(user);

        // when
        String keyword = "test";
        List<PtProcessResponseDto> response = processService.findAll(keyword);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response).isSortedAccordingTo(Comparator.comparingLong(PtProcessResponseDto::getId).reversed());
    }

    @Test
    @DisplayName("날짜별 필터링으로 조회하기")
    void findAllByDate() {
        // given
        LocalDate searchStartDate = LocalDate.of(2023, 12, 4);
        LocalDate searchEndDate = LocalDate.of(2023, 12, 8);

        // when
        List<PtProcessResponseDto> process = processService.findAllByDate(searchStartDate, searchEndDate);

        // then
        assertThat(process).isNotNull();
        assertThat(process).isNotEmpty();
    }
}