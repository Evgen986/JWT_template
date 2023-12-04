package ru.maliutin.tasklist.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.service.AuthService;
import ru.maliutin.tasklist.service.UserService;
import ru.maliutin.tasklist.web.dto.aut.JwtRequest;
import ru.maliutin.tasklist.web.dto.aut.JwtResponse;
import ru.maliutin.tasklist.web.dto.user.UserDto;
import ru.maliutin.tasklist.web.dto.validation.OnCreate;
import ru.maliutin.tasklist.web.mappers.UserMapper;

/**
 * Контроллер отвечающий за авторизацию пользователей.
 */

// Аннотация Spring - контроллер отвечающий JSON ответами.
@RestController
// Аннотация Spring - адрес обрабатываемый контроллером.
@RequestMapping("/api/v1/auth")
// Аннотация lombok - используется для автоматической генерации конструктора, исходя из аргументов полей класса, которые отмечены другой аннотацией Lombok, такой как @NonNull.
@RequiredArgsConstructor
// Аннотация Spring - активирует валидацию для всех методов контроллера.
@Validated
@Tag(name = "Auth Controller", description = "Auth API")
// Аннотация Swagger добавляющая в документацию название и описание контроллера.
public class AuthController {
    /**
     * Поле сервиса аутентификации.
     */
    private final AuthService authService;
    /**
     * Поле сервиса для работы с сущностью пользователя.
     */
    private final UserService userService;
    /**
     * Поле объекта преобразования сущности пользователя.
     */
    private final UserMapper userMapper;

    /**
     * Аутентификация пользователя.
     *
     * @param loginRequest принимает запрос пользователя в виде jwt запроса (с токеном)
     * @return jwt ответ с парой токенов.
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate user") // Аннотация Swagger добавляющая описание метода в документацию.
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * Регистрация пользователя.
     *
     * @param userDto объект передачи данных User
     * @return объект пользователя.
     */
    @PostMapping("/register")
    @Operation(summary = "Registration user") // Аннотация Swagger добавляющая описание метода в документацию.
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    /**
     * TODO
     * Метод обновления пары токенов.
     *
     * @param refreshToken токен для обновления
     * @return
     */
    @Operation(summary = "Refresh token") // Аннотация Swagger добавляющая описание метода в документацию.
    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
