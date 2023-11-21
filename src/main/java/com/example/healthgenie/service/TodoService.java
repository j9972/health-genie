package com.example.healthgenie.service;

import com.example.healthgenie.domain.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.domain.ptreview.dto.PtReviewResponseDto;
import com.example.healthgenie.domain.ptreview.entity.PtReview;
import com.example.healthgenie.domain.todo.dto.TodoRequestDto;
import com.example.healthgenie.domain.todo.dto.TodoResponseDto;
import com.example.healthgenie.domain.todo.entity.Todo;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.exception.*;
import com.example.healthgenie.global.config.SecurityUtil;
import com.example.healthgenie.repository.TodoRepository;
import com.example.healthgenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public TodoResponseDto addTodoList(TodoRequestDto dto, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PtReviewException(PtReviewErrorResult.NO_USER_INFO));

        return addTodo(dto, user);
    }

    @Transactional
    public TodoResponseDto addTodo(TodoRequestDto dto, User user){
        Todo todo = Todo.builder()
                .todoDate(dto.getTodoDate())
                .todoTime(dto.getTodoTime())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .member(user)
                .build();
        Todo saved = todoRepository.save(todo);

        return TodoResponseDto.of(saved);
    }

    @Transactional
    public Long update(TodoRequestDto dto, Long todoId){

        Todo todo = authorizationWriter(todoId);

        if(dto.getTodoDate() != null) {
            todo.updateDate(dto.getTodoDate());
        }
        if(dto.getTodoTime() != null) {
            todo.updateTime(dto.getTodoTime());
        }
        if(dto.getTitle() != null) {
            todo.updateTitle(dto.getTitle());
        }
        if(dto.getDescription() != null) {
            todo.updateDescription(dto.getDescription());
        }
        if(dto.getStatus() != null) {
            todo.updateStatus(dto.getStatus());
        }

        return todoId;
    }

    public void deletePtReview(Long todoId) {
        // user가 해당 글을 작성한 유저인지 체크 [ DB에 많은 데이터가 쌓이기 때문에 필요 ]
        Todo todo = authorizationWriter(todoId);
        todoRepository.deleteById(todo.getId());
    }

    public User isMemberCurrent() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .orElseThrow(() ->  new TodoException(TodoErrorResult.NO_USER_INFO));
    }

    public Todo authorizationWriter(Long id) {
        User member = isMemberCurrent();

        Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoException(TodoErrorResult.NO_TODO_INFO));
        if (!todo.getMember().equals(member)) {
            throw new TodoException(TodoErrorResult.WRONG_USER);
        }
        return todo;
    }

    public List<TodoResponseDto> getAllMyTodo(Long userId) {
        // 날짜가 맞으면
        List<Todo> todos = todoRepository.findAllByMemberId(userId);
        return todos.stream()
                .map(TodoResponseDto::of)
                .collect(toList());
    }
}