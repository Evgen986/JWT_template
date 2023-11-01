package ru.maliutin.tasklist.web.dto.aut;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Класс авторизации пользователя.
 */
@Data
public class JwtRequest {
    /**
     * Поле с логином пользователя.
     */
    @NotNull(message = "Логин не может быть пусты!")  // Аннотация валидации - проверяющая, что поле не пустое.
    private String username;

    /**
     * Поле с паролем пользователя.
     */
    @NotNull(message = "Пароль не может быть пустым!")  // Аннотация валидации - проверяющая, что поле не пустое.
    private String password;
}
