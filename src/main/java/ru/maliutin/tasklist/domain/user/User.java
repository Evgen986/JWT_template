package ru.maliutin.tasklist.domain.user;

import lombok.Data;
import ru.maliutin.tasklist.domain.task.Task;

import java.util.List;
import java.util.Set;


/**
 * Класс модели пользователя.
 */
@Data  // Автоматически создает геттеры, сеттеры, конструкторы, hashcode, equals;
public class User {
    /**
     * Id пользователя.
     */
    private long id;
    /**
     * Имя пользователя.
     */
    private String name;
    /**
     * Логин пользователя (email).
     */
    private String username;
    /**
     * Пароль пользователя.
     */
    private String password;
    /**
     * Подтверждение пароля пользователя.
     */
    private String passwordConfirmation;
    /**
     * Коллекция ролей пользователя.
     */
    private Set<Role> roles;
    /**
     * Задачи пользователя.
     */
    private List<Task> tasks;

}
