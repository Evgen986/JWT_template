package ru.maliutin.tasklist.web.dto.aut;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Класс авторизации пользователя.
 */
@Data
@Schema(description = "Request for login") // Аннотация Swagger добавляющая описание схемы в документации
public class JwtRequest {
    /**
     * Поле с логином пользователя.
     */
    @NotNull(message = "Логин не может быть пусты!")  // Аннотация валидации - проверяющая, что поле не пустое.
    @Schema(description = "email", example = "johndoe@gmail.com")
    private String username;

    /**
     * Поле с паролем пользователя.
     */
    @NotNull(message = "Пароль не может быть пустым!")  // Аннотация валидации - проверяющая, что поле не пустое.
    @Schema(description = "password", example = "12345")
    private String password;
}
