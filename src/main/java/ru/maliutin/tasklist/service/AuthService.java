package ru.maliutin.tasklist.service;

import ru.maliutin.tasklist.web.dto.aut.JwtRequest;
import ru.maliutin.tasklist.web.dto.aut.JwtResponse;

/**
 * Интерфейс сервиса аутентификации.
 */
public interface AuthService {
    /**
     *
     * @param loginRequest
     * @return
     */
    JwtResponse login(JwtRequest loginRequest);

    /**
     *
     * @param refreshToken
     * @return
     */
    JwtResponse refresh(String refreshToken);

}
