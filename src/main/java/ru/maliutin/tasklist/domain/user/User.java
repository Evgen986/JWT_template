package ru.maliutin.tasklist.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Transient;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import ru.maliutin.tasklist.domain.task.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * Класс модели пользователя.
 */
@Entity
@Table(name = "users")
// Автоматически создает геттеры, сеттеры, конструкторы, hashcode, equals;
@Data
public class User implements Serializable {
    /**
     * Id пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Transient
    private String passwordConfirmation;

    /**
     * Коллекция ролей пользователя.
     */
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    /**
     * Задачи пользователя.
     */
    @CollectionTable(name = "users_tasks")
    @OneToMany
    @JoinColumn(name = "task_id")
    private List<Task> tasks;
}
