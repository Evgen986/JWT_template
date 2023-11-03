package ru.maliutin.tasklist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.service.AuthService;
import ru.maliutin.tasklist.service.UserService;
import ru.maliutin.tasklist.web.dto.aut.JwtRequest;
import ru.maliutin.tasklist.web.dto.aut.JwtResponse;
import ru.maliutin.tasklist.web.security.JwtTokenProvider;

/**
 * Класс реализующий интерфейс AuthService и содержащий бизнес-логику программы.
 * Осуществляет аутентификацию пользователя.
 */
@Service  // Аннотация обозначающая класс как объект сервиса для Spring
// Аннотация lombok - используется для автоматической генерации конструктора, исходя из аргументов полей класса, которые отмечены другой аннотацией Lombok, такой как @NonNull.
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /**
     * Поле с объектом для аутентификации пользователя.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * Поле с объектом сервиса для получения пользователя из БД.
     */
    private final UserService userService;
    /**
     * Поле с объектом для создания токена.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Метод аутентификации пользователя.
     * @param loginRequest запрос на аутентификацию.
     * @return
     */
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        /*
            Таким образом происходит перенаправление Spring на собственный класс аутентификации
            JwtUserDetailService в котором проводится аутентификация пользователя в методе loadUserByUserName()
         */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        // Если аутентификация прошла успешно и не было выброшено исключений
        // Используя сервис получаем пользователя из БД
        User user = userService.getByUsername(loginRequest.getUsername());
        // Заполняем Jwt ответ
        // Идентификатор пользователя
        jwtResponse.setId(user.getId());
        // Логин пользователя
        jwtResponse.setUsername(user.getUsername());
        // Короткоживущий токен
        jwtResponse.setAccessToken(
                jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        // Долгоживущий токен
        jwtResponse.setRefreshToken(
                jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));
        return jwtResponse;
    }

    /**
     * Метод производящий обновление пары токенов.
     * @param refreshToken долгоживущий токен
     * @return ответ с парой токенов.
     */
    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }
}
