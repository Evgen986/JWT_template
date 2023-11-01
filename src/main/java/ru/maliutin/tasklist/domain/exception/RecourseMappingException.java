package ru.maliutin.tasklist.domain.exception;

/**
 * Исключение выбрасывается когда в JDBC происходят ошибки при получении объектов из БД.
 */
public class RecourseMappingException extends RuntimeException{
    /**
     * Конструктор класса исключения, использует родительский конструктор RuntimeException.
     * @param message сообщение для пользователя.
     */
    public RecourseMappingException(String message) {
        super(message);
    }
}
