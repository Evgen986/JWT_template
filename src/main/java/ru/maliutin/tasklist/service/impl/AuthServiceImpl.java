package ru.maliutin.tasklist.service.impl;

import org.springframework.stereotype.Service;
import ru.maliutin.tasklist.service.AuthService;
import ru.maliutin.tasklist.web.dto.aut.JwtRequest;
import ru.maliutin.tasklist.web.dto.aut.JwtResponse;

/**
 * Класс реализующий интерфейс AuthService и содержащий бизнес-логику программы.
 * Осуществляет аутентификацию пользователя.
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
