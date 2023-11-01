package ru.maliutin.tasklist.repository.impl;

import org.springframework.stereotype.Repository;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * Класс реализующий функциональность репозитория объектов задач.
 */
@Repository  // Аннотация обозначающая класс как объект репозитория для Spring
public class TaskRepositoryImpl implements TaskRepository {

    @Override
    public Optional<Task> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(long userId) {
        return null;
    }

    @Override
    public void assignToUserById(long taskId, long userId) {

    }

    @Override
    public void update(Task task) {

    }

    @Override
    public void create(Task task) {

    }

    @Override
    public void delete(long id) {

    }
}
