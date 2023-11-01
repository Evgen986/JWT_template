package ru.maliutin.tasklist.repository;

import ru.maliutin.tasklist.domain.task.Task;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для запросов к БД сущности Task.
 */
public interface TaskRepository {
    /**
     * Поиск задачи по Id.
     * @param id идентификатор задачи.
     * @return объект Optional, который как может содержать задачу, так и нет.
     */
    Optional<Task> findById(long id);

    /**
     * Получение всех задач для конкретного пользователя.
     * @param userId идентификатор пользователя.
     * @return лист задач.
     */
    List<Task> findAllByUserId(long userId);

    /**
     * Связывает задачу с конкретным пользователем.
     * @param taskId идентификатор задачи.
     * @param userId идентификатор пользователя.
     */
    void assignToUserById(long taskId, long userId);

    /**
     * Обновление задачи в БД.
     * @param task объект задачи.
     */
    void update(Task task);

    /**
     * Сохранение новой задачи.
     * @param task объект задачи.
     */
    void create(Task task);

    /**
     * Удаление задачи из БД.
     * @param id идентификатор задачи.
     */
    void delete(long id);

}
