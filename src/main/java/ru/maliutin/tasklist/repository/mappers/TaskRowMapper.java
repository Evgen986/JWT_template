package ru.maliutin.tasklist.repository.mappers;

import lombok.SneakyThrows;
import ru.maliutin.tasklist.domain.task.Status;
import ru.maliutin.tasklist.domain.task.Task;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс преобразующий ответ из БД в объект Task.
 */
public class TaskRowMapper {
    /**
     * Метод преобразования одной задачи в ответе из БД, в объект задачи.
     * @param resultSet ответ из БД.
     * @return объект задачи.
     */
    @SneakyThrows // Аннотация lombok - в случае возникновения исключения пробрасывает его выше
    public static Task mapRow(ResultSet resultSet){
        // Если есть что преобразовывать
        if (resultSet.next()){
            // Создаем объект задачи. И заполняем его поля.
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            task.setTitle(resultSet.getString("task_title"));
            task.setDescription(resultSet.getString("task_description"));
            task.setStatus(Status.valueOf(resultSet.getString("task_status")));
            Timestamp timestamp = resultSet.getTimestamp("task_expiration_date");
            // Проверка что-бы в поле времени выполнения задачи не было null
            if (timestamp != null){
                task.setExpirationDate(timestamp.toLocalDateTime());
            }
            return task;
        }
        return null;
    }

    /**
     * Метод преобразования множества задач из ответа БД, в список объектов задач.
     * @param resultSet ответ из БД.
     * @return список объектов задач.
     */
    @SneakyThrows // Аннотация lombok - в случае возникновения исключения пробрасывает его выше
    public static List<Task> mapRows(ResultSet resultSet){
        List<Task> tasks = new ArrayList<>();
        // Если есть что преобразовывать
        while (resultSet.next()){
            // Создаем объект задачи. И заполняем его поля.
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            if(!resultSet.wasNull()) {
                task.setTitle(resultSet.getString("task_title"));
                task.setDescription(resultSet.getString("task_description"));
                task.setStatus(Status.valueOf(resultSet.getString("task_status")));
                Timestamp timestamp = resultSet.getTimestamp("task_expiration_date");
                // Проверка что-бы в поле времени выполнения задачи не было null
                if (timestamp != null) {
                    task.setExpirationDate(timestamp.toLocalDateTime());
                }
                tasks.add(task);
            }
        }
        return tasks;
    }
}
