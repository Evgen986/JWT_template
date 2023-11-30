package ru.maliutin.tasklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.maliutin.tasklist.domain.task.Task;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для запросов к БД сущности Task.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {


    /**
     * Получение всех задач для конкретного пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return лист задач.
     */
    @Query(value = """
            SELECT * FROM tasks t
            JOIN users_tasks ut ON ut.task_id = t.id
            WHERE ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") long userId);

}
