package ru.maliutin.tasklist.domain.exception;

/**
 * Исключение выбрасывается при проблемах загрузки изображения.
 */
public class ImageUploadException extends RuntimeException{
    /**
     * Конструктор исключения.
     * Вызывает родительский конструктор класса RuntimeException.
     */
    public ImageUploadException(String message) {
        super(message);
    }

}
