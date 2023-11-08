package ru.maliutin.tasklist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maliutin.tasklist.domain.exception.ResourceNotFoundException;
import ru.maliutin.tasklist.domain.task.Status;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.repository.TaskRepository;
import ru.maliutin.tasklist.service.TaskService;

import java.util.List;
/**
 * Класс реализующий интерфейс TaskService и содержащий бизнес-логику программы.
 * Осуществляет запросы к репозиторию и взаимодействующий с моделью Task.
 */
@Service  // Аннотация обозначающая класс как объект сервиса для Spring
@RequiredArgsConstructor  // Аннотация lombok - используется для автоматической генерации конструктора, исходя из аргументов полей класса
@Transactional(readOnly = true)  // Аннотация указывающая, что в классе производятся транзакции при обращении к БД
public class TaskServiceImpl implements TaskService {
    /**
     * Поле с репозиторием объекта Task.
     */
    private final TaskRepository taskRepository;

    /**
     * Получение задачи по идентификатору.
     * @param id идентификатор задачи.
     * @throws ResourceNotFoundException задача не найдена.
     * @return объект задачи.
     */
    @Override
    public Task getById(long id) throws ResourceNotFoundException{
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    /**
     * Получение списка задач по идентификатору пользователя.
     * @param userId идентификатор пользователя.
     * @return список задач.
     */
    @Override
    public List<Task> getAllByUserId(long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    /**
     * Обновление задачи.
     * @param task объект задачи.
     * @return обновленную задачу.
     */
    @Override
    @Transactional
    public Task update(Task task) {
        if (task.getStatus() == null){
            task.setStatus(Status.TODO);
        }
        taskRepository.update(task);
        return task;
    }

    /**
     * Создание новой задачи.
     * @param task объект задачи.
     * @param userId  идентификатор пользователя, которому принадлежит задача.
     * @return созданную задачу.
     */
    @Override
    @Transactional
    public Task create(Task task, long userId) {
        task.setStatus(Status.TODO);
        taskRepository.create(task);
        taskRepository.assignToUserById(task.getId(), userId);
        return task;
    }

    /**
     * Удаление задачи.
     * @param id идентификатор задачи.
     */
    @Override
    @Transactional
    public void delete(long id) {
        taskRepository.delete(id);
    }
}
