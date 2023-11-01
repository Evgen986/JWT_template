package ru.maliutin.tasklist.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.service.TaskService;
import ru.maliutin.tasklist.web.dto.task.TaskDto;
import ru.maliutin.tasklist.web.dto.validation.OnUpdate;
import ru.maliutin.tasklist.web.mappers.TaskMapper;

/**
 * Класс контроллера обрабатывающий запросы к задачам (Task).
 */
// Аннотация Spring - контроллер отвечающий JSON ответами.
@RestController
// Аннотация Spring - адрес обрабатываемый контроллером.
@RequestMapping("/api/v1/tasks")
// Аннотация lombok - используется для автоматической генерации конструктора, исходя из аргументов полей класса, которые отмечены другой аннотацией Lombok, такой как @NonNull.
@RequiredArgsConstructor
// Аннотация Spring - активирует валидацию для всех методов контроллера.
@Validated
public class TaskController {
    /**
     * Поле интерфейса сервиса объектов задач (Task).
     */
    private final TaskService taskService;
    /**
     * Поле интерфейса маппера преобразований объектов задач (Task).
     */
    private final TaskMapper taskMapper;

    /**
     * Получение задачи по id.
     * @param id идентификатор задачи
     * @return задачу в виде объекта передачи данных.
     */
    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable Long id){
        return taskMapper.toDto(taskService.getById(id));
    }

    /**
     * Удаление задачи по id.
     * @param id идентификатор задачи.
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        taskService.delete(id);
    }

    /**
     * Обновление задачи.
     * @param taskDto задача для обновления.
     * @return обновленную задачу.
     */
    @PutMapping()
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto taskDto){
        Task task = taskMapper.toEntity(taskDto);
        Task updateTusk = taskService.update(task);
        return taskMapper.toDto(updateTusk);
    }

}

