package ru.maliutin.tasklist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maliutin.tasklist.domain.exception.ResourceNotFoundException;
import ru.maliutin.tasklist.domain.task.Status;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.domain.task.TaskImage;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.repository.TaskRepository;
import ru.maliutin.tasklist.service.ImageService;
import ru.maliutin.tasklist.service.TaskService;
import ru.maliutin.tasklist.service.UserService;

import java.util.List;

/**
 * Класс реализующий интерфейс TaskService и содержащий бизнес-логику программы.
 * Осуществляет запросы к репозиторию и взаимодействующий с моделью Task.
 */
// Аннотация обозначающая класс как объект сервиса для Spring
@Service
@RequiredArgsConstructor
/*
    Аннотация lombok -
    используется для автоматической генерации
    конструктора, исходя из аргументов полей класса
 */
// Аннотация указывающая,
// что в классе производятся транзакции при обращении к БД
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    /**
     * Поле с репозиторием объекта Task.
     */
    private final TaskRepository taskRepository;

    private final UserService userService;

    private final ImageService imageService;

    /**
     * Получение задачи по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return объект задачи.
     * @throws ResourceNotFoundException задача не найдена.
     */
    @Override
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(final long id) throws ResourceNotFoundException {
        return taskRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found."));
    }

    /**
     * Получение списка задач по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return список задач.
     */
    @Override
    public List<Task> getAllByUserId(final long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    /**
     * Обновление задачи.
     *
     * @param task объект задачи.
     * @return обновленную задачу.
     */
    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(final Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    /**
     * Создание новой задачи.
     *
     * @param task   объект задачи.
     * @param userId идентификатор пользователя, которому принадлежит задача.
     * @return созданную задачу.
     */
    @Override
    @Transactional
    @Cacheable(value = "TaskService:getById", key = "#task.id")
    public Task create(final Task task,
                       final long userId) {
        User user = userService.getById(userId);
        task.setStatus(Status.TODO);
        user.getTasks().add(task);
        userService.update(user);
        return task;
    }

    /**
     * Удаление задачи.
     *
     * @param id идентификатор задачи.
     */
    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(final long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#taskId")
    public void uploadImage(
            final Long taskId,
            final TaskImage image) {
        Task task = getById(taskId);
        String fileName = imageService.upload(image);
        task.getImages().add(fileName);
        taskRepository.save(task);
    }
}
