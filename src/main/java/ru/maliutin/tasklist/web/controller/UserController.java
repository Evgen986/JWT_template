package ru.maliutin.tasklist.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.service.TaskService;
import ru.maliutin.tasklist.service.UserService;
import ru.maliutin.tasklist.web.dto.task.TaskDto;
import ru.maliutin.tasklist.web.dto.user.UserDto;
import ru.maliutin.tasklist.web.dto.validation.OnCreate;
import ru.maliutin.tasklist.web.dto.validation.OnUpdate;
import ru.maliutin.tasklist.web.mappers.TaskMapper;
import ru.maliutin.tasklist.web.mappers.UserMapper;

import java.util.List;

/**
 * Класс контроллера обрабатывающий запросы к пользователям (User).
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    /**
     * Поле интерфейса сервиса объектов пользователя (User).
     */
    private final UserService userService;
    /**
     * Поле интерфейса сервиса объектов задач (Task).
     */
    private final TaskService taskService;

    /**
     * Поле маппера объектов пользователя (User).
     */
    private final UserMapper userMapper;
    /**
     * Поле маппера объектов задач (Task).
     */
    private final TaskMapper taskMapper;

    /**
     * Обновление пользователя.
     * @param userDto пользователь для обновления.
     * @return обновленный пользователь.
     */
    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto){
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Получение пользователя по id.
     * @param id идентификатор пользователя.
     * @return найденный пользователь.
     */
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    /**
     * Удаление пользователя.
     * @param id идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        userService.delete(id);
    }

    /**
     * Получение всех задач конкретного пользователя.
     * @param id идентификатор пользователя.
     * @return список задач пользователя.
     */
    @GetMapping("/{id}/tasks")
    public List<TaskDto> getTasksByUserId(@PathVariable Long id){
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    /**
     * Создание новой задачи.
     * @param id идентификатор пользователя, которому принадлежит задача.
     * @param taskDto новая задача.
     * @return созданную задачу.
     */
    @PostMapping("/{id}/tasks")
    public TaskDto createTask(@PathVariable Long id, @Validated(OnCreate.class) @RequestBody TaskDto taskDto){
        Task newTask = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(newTask, id);
        return taskMapper.toDto(createdTask);
    }
}
