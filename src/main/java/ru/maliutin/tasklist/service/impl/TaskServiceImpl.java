package ru.maliutin.tasklist.service.impl;

import org.springframework.stereotype.Service;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.service.TaskService;

import java.util.List;
/**
 * Класс реализующий интерфейс TaskService и содержащий бизнес-логику программы.
 * Осуществляет запросы к репозиторию и взаимодействующий с моделью Task.
 */
@Service  // Аннотация обозначающая класс как объект сервиса для Spring
public class TaskServiceImpl implements TaskService {
    @Override
    public Task getById(long id) {
        return null;
    }

    @Override
    public List<Task> getAllByUserId(long userId) {
        return null;
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public Task create(Task task, long id) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
