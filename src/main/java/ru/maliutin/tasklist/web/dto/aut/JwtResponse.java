package ru.maliutin.tasklist.web.dto.aut;

import lombok.Data;

/**
 *
 */
@Data
public class JwtResponse {
    /**
     * Поле с идентификатором пользователя.
     */
    private long id;
    /**
     * Поле с логином пользователя.
     */
    private String username;
    /**
     * Поле с ... токеном.
     */
    private String accessToken;
    /**
     * Поле с ... токеном.
     */
    private String refreshToken;
}
