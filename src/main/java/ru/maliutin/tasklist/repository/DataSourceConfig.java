package ru.maliutin.tasklist.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Фабрика соединений с БД.
 */

@Configuration  // Аннотация указывает Spring, что этот класс является частью конфигурации приложения.
@RequiredArgsConstructor // Аннотация Lombok - генерирует конструктор, который автоматически принимает аргументы для всех полей класса, помеченных как final
public class DataSourceConfig {

    /**
     * Поле с источником данных (конфигурируется application.yaml)
     */
    private final DataSource dataSource;

    /**
     * Получение соединения с источником данных.
     * @return объект соединения.
     */
    public Connection getConnection(){
        return DataSourceUtils.getConnection(dataSource);
    }
}
