package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessPhotoRepository;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewRepository;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.todo.repository.TodoRepository;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfilePhotoRepository;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfileRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestSyUtils {

    private final RoutineRepository routineRepository;
    private final TodoRepository todoRepository;
    private final PtReviewRepository ptReviewRepository;
    private final TrainerProfileRepository trainerProfileRepository;
    private final TrainerProfilePhotoRepository trainerProfilePhotoRepository;
    private final PtProcessRepository ptProcessRepository;
    private final PtProcessPhotoRepository ptProcessPhotoRepository;
    private final UserRepository userRepository;

    public boolean notLogin(User user) {
        return false;
    }

    public User createUser(String uniName, String email) {
        User user = User.builder()
                .uniName(uniName)
                .email(email)
                .build();

        return userRepository.save(user);
    }

    public RoutineRequestDto createOwnRoutineRequest(Day day, String parts, List<WorkoutRecipe> recipes, String writer) {

        return RoutineRequestDto.builder()
                .day(day)
                .parts(parts)
                .workoutRecipes(recipes)
                .writer(writer)
                .build();
    }


    public Routine writeRoutine(Day day, String parts, List<WorkoutRecipe> recipes, User writer) {

        Routine saved = new Routine();

        for (WorkoutRecipe data : recipes) {

            WorkoutRecipe recipe = new WorkoutRecipe(data.getName(), data.getKg(), data.getSets(), data.getReps());

            Routine routine = Routine.builder()
                    .day(day)
                    .parts(parts)
                    .workoutRecipe(recipe)
                    .member(writer)
                    .build();

            saved = routineRepository.save(routine);
        }
        return saved;

    }

    public Routine genieRoutine(Level level, Day day, String content, String parts, WorkoutRecipe recipe) {
        Routine routine = Routine.builder()
                .level(level)
                .day(day)
                .content(content)
                .parts(parts)
                .workoutRecipe(recipe)
                .build();

        return routineRepository.save(routine);
    }

    public TodoRequestDto TodoRequestDto(String title, String description) {
        return TodoRequestDto(null, null, title, description);
    }

    public TodoRequestDto TodoRequestDto(LocalDate date, LocalTime time, String title, String description) {

        return TodoRequestDto.builder()
                .date(date)
                .time(time)
                .title(title)
                .description(description)
                .build();
    }

    public Todo createTodo(LocalDate date, LocalTime time, String title, String description, User user) {

        Todo todo = Todo.builder()
                .date(date)
                .time(time)
                .title(title)
                .description(description)
                .member(user)
                .build();

        return todoRepository.save(todo);
    }

    public PtReviewRequestDto createReviewDto(String content, String stopReason, Double reviewScore,
                                           String userNickname, String trainerNickname) {
        return PtReviewRequestDto.builder()
                .content(content)
                .stopReason(stopReason)
                .reviewScore(reviewScore)
                .userNickName(userNickname)
                .trainerNickName(trainerNickname)
                .build();
    }

    public PtReview createReview(String content, String stopReason, Double reviewScore,
                                 User user, User trainer) {
        PtReview review =  PtReview.builder()
                .content(content)
                .stopReason(stopReason)
                .reviewScore(reviewScore)
                .member(user)
                .trainer(trainer)
                .build();

        return ptReviewRepository.save(review);
    }

    public PtProcessRequestDto createProcessDto(LocalDate date, String title, String content) {
        return createProcessDto(date, title, content, null);
    }

    public PtProcessRequestDto createProcessDto(LocalDate date, String title, String content,
                                                List<MultipartFile> photos) {
        return PtProcessRequestDto.builder()
                .date(date)
                .content(content)
                .title(title)
                .photos(photos)
                .build();
    }

    public PtProcess createProcess(LocalDate date, String title, String content,
                                   List<PtProcessPhoto> photos,
                                   User user, User trainer) {
        PtProcess process = PtProcess.builder()
                .date(date)
                .title(title)
                .content(content)
                .ptProcessPhotos(photos)
                .ptProcessPhotos(new ArrayList<>())
                .member(user)
                .trainer(trainer)
                .build();

        return ptProcessRepository.save(process);
    }

    public PtProcess createProcess(LocalDate date, String title, String content,
                                   User user, User trainer) {
        return createProcess(date, title, content, null, user, trainer);
    }

    public PtProcess createProcess(String title, String content, User user, User trainer) {
        return createProcess(null, title, content, null, user, trainer);
    }

    public PtProcessPhoto createProcessPhoto(PtProcess process, String path) {
        PtProcessPhoto processPhoto = PtProcessPhoto.builder()
                .process(process)
                .processPhotoPath(path)
                .build();

        return ptProcessPhotoRepository.save(processPhoto);
    }

    public ProfileRequestDto createProfileDto(String introduction,String career, String university,
                                              LocalTime startTime, LocalTime endTime, Double reviewAvg,
                                              int cost, int month, String nickname) {
        return createProfileDto(introduction, career, university,startTime,endTime,reviewAvg, null,
                cost, month,nickname);
    }

    public ProfileRequestDto createProfileDto(String introduction,String career, String university,
                                              LocalTime startTime, LocalTime endTime, Double reviewAvg,
                                              List<MultipartFile> photos, int cost,
                                              int month, String nickname) {
        return ProfileRequestDto.builder()
                .introduction(introduction)
                .career(career)
                .university(university)
                .startTime(startTime)
                .endTime(endTime)
                .reviewAvg(reviewAvg)
                .photos(photos)
                .cost(cost)
                .month(month)
                .nickname(nickname)
                .build();
    }

    public TrainerInfo createProfile(String introduction, String career, String university,
                                     LocalTime startTime, LocalTime endTime, Double reviewAvg,
                                     int cost, int month, User user) {
        return createProfile(introduction, career, university, startTime, endTime, reviewAvg, null, cost, month, user);
    }

    public TrainerInfo createProfile(String introduction, String career, String university,
                                     LocalTime startTime, LocalTime endTime, Double reviewAvg,
                                     List<TrainerPhoto> photos, int cost,
                                     int month, User user) {
        TrainerInfo profile = TrainerInfo.builder()
                .introduction(introduction)
                .career(career)
                .cost(cost)
                .careerMonth(month)
                .university(university)
                .startTime(startTime)
                .endTime(endTime)
                .reviewAvg(reviewAvg)
                .trainerPhotos(photos)
                .trainerPhotos(new ArrayList<>())
                .member(user)
                .build();

        return trainerProfileRepository.save(profile);
    }


    public TrainerPhoto createProfilePhoto(TrainerInfo profile, String path) {
        TrainerPhoto profilePhoto = TrainerPhoto.builder()
                .info(profile)
                .infoPhotoPath(path)
                .build();

        return trainerProfilePhotoRepository.save(profilePhoto);
    }

}
