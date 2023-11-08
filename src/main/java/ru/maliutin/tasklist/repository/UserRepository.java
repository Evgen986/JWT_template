package ru.maliutin.tasklist.repository;

import ru.maliutin.tasklist.domain.user.Role;
import ru.maliutin.tasklist.domain.user.User;

import java.util.Optional;

/**
 * Интерфейс для запросов к БД сущности User.
 */
public interface UserRepository {
    /**
     * Поиск пользователя по Id.
     * @param id идентификатор пользователя.
     * @return объект Optional, который как может содержать пользователя, так и нет.
     */
    Optional<User> findById(long id);

    /**
     * Поиск пользователя по username (логину).
     * @param username логин пользователя.
     * @return объект Optional, который как может содержать пользователя, так и нет.
     */
    Optional<User> findByUsername(String username);

    /**
     * Обновление пользователя в БД.
     * @param user объект пользователя.
     */
    void update(User user);

    /**
     * Сохранение нового пользователя.
     * @param user объект пользователя.
     */
    void create(User user);

    /**
     * Сохранение роли пользователя.
     * @param id идентификатор пользователя.
     * @param role роль пользователя.
     */
    void insertUserRole(long id, Role role);

    /**
     * Проверка является ли пользователь владельцем задачи.
     * @param userId идентификатор пользователя.
     * @param taskId идентификатор задачи.
     * @return true - пользователь является владельцем, иначе - false.
     */
    boolean isTaskOwner(long userId, long taskId);

    /**
     * Удаление пользователя из БД.
     * @param id идентификатор пользователя.
     */
    void delete(long id);
}
