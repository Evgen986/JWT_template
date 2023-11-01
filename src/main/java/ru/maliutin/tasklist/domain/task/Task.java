package ru.maliutin.tasklist.domain.task;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Класс модели задачи пользователя.
 */
@Data  // Автоматически создает геттеры, сеттеры, конструкторы, hashcode, equals;
public class Task {
    /**
     * Id задачи.
     */
    private long id;
    /**
     * Заголовок задачи.
     */
    private String title;
    /**
     * Описание задачи.
     */
    private String description;
    /**
     * Статус задачи (TODO, IN_PROGRESS, DONE).
     */
    private Status status;
    /**
     * Время до которого должна быть выполнена задача.
     */
    private LocalDateTime expirationDate;

}
