package ru.maliutin.tasklist.repository.mappers;

import lombok.SneakyThrows;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.domain.user.Role;
import ru.maliutin.tasklist.domain.user.User;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс преобразования ответов из БД в объект пользователя.
 */
public class UserRowMapper {
    /**
     * Метод преобразующий ответ из БД в объект пользователя.
     * @param resultSet ответ из БД.
     * @return объект пользователя.
     */
    @SneakyThrows
    public static User mapRow(ResultSet resultSet){
        // Получаем роли пользователя
        Set<Role> roles = new HashSet<>();
        while (resultSet.next()){
            roles.add(Role.valueOf(resultSet.getString("user_role_role")));
        }

        // Возвращаемся в начало resultSet
        resultSet.beforeFirst();

        // Получаем задачи пользователя
        List<Task> tasks = TaskRowMapper.mapRows(resultSet);

        // Возвращаемся в начало resultSet
        resultSet.beforeFirst();

        // Получаем пользователя
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setName(resultSet.getString("user_name"));
            user.setUsername(resultSet.getString("user_username"));
            user.setPassword(resultSet.getString("user_password"));

            // Добавляем пользователю задачи и роли
            user.setTasks(tasks);
            user.setRoles(roles);
            return user;
        }
        return null;
    }

}
