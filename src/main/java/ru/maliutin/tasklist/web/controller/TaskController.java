package ru.maliutin.tasklist.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.domain.task.TaskImage;
import ru.maliutin.tasklist.service.TaskService;
import ru.maliutin.tasklist.web.dto.task.TaskDto;
import ru.maliutin.tasklist.web.dto.task.TaskImageDto;
import ru.maliutin.tasklist.web.dto.validation.OnUpdate;
import ru.maliutin.tasklist.web.mappers.TaskImageMapper;
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
@Tag(name = "Task Controller", description = "Task API") // Аннотация Swagger добавляющая в документацию название и описание контроллера.
public class TaskController {
    /**
     * Поле интерфейса сервиса объектов задач (Task).
     */
    private final TaskService taskService;
    /**
     * Поле интерфейса маппера преобразований объектов задач (Task).
     */
    private final TaskMapper taskMapper;

    private final TaskImageMapper taskImageMapper;

    /**
     * Получение задачи по id.
     * @param id идентификатор задачи
     * @return задачу в виде объекта передачи данных.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get TaskDTO by id") // Аннотация Swagger добавляющая описание метода в документацию.
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public TaskDto getById(@PathVariable Long id){
        return taskMapper.toDto(taskService.getById(id));
    }

    /**
     * Удаление задачи по id.
     * @param id идентификатор задачи.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by id") // Аннотация Swagger добавляющая описание метода в документацию.
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void deleteById(@PathVariable Long id){
        taskService.delete(id);
    }

    /**
     * Обновление задачи.
     * @param taskDto задача для обновления.
     * @return обновленную задачу.
     */
    @PutMapping()
    @Operation(summary = "Update task") // Аннотация Swagger добавляющая описание метода в документацию.
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto taskDto){
        Task task = taskMapper.toEntity(taskDto);
        Task updateTusk = taskService.update(task);
        return taskMapper.toDto(updateTusk);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void uploadImage(@PathVariable("id") Long id,
                            @Validated
                            @ModelAttribute TaskImageDto imageDto){
        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, image);
    }
}

