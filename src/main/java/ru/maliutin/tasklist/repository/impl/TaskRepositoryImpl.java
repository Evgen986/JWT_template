package ru.maliutin.tasklist.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.maliutin.tasklist.domain.exception.ResourceMappingException;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.repository.DataSourceConfig;
import ru.maliutin.tasklist.repository.TaskRepository;
import ru.maliutin.tasklist.repository.mappers.TaskRowMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * Класс реализующий функциональность репозитория объектов задач.
 */
@Repository  // Аннотация обозначающая класс как объект репозитория для Spring
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    // Фабрика соединений с БД.
    private final DataSourceConfig dataSourceConfig;
    // SQL запросы к БД.
    private final String FIND_BY_ID = """
            SELECT t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM tasks t
            WHERE id = ?""";

    private final String FIND_ALL_BY_USER_ID = """
            SELECT t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM tasks t
                     left outer join users_tasks ut on t.id = ut.task_id
            WHERE ut.user_id = ?""";
    private final String ASSIGN = """
            INSERT INTO users_tasks (task_id, user_id)
            VALUES (?, ?)""";
    private final String UPDATE = """
            UPDATE tasks
            SET title = ?,
                description = ?,
                expiration_date = ?,
                status = ?
            where id = ?""";

    private final String CREATE = """
            INSERT INTO tasks (title, description, expiration_date, status)
            VALUES (?, ?, ?, ?)""";
    private final String DELETE = """
            DELETE
            FROM tasks
            WHERE id = ?""";

    /**
     * Получение задачи по ее идентификатору.
     * @param id идентификатор задачи.
     * @return объект задачи.
     */
    @Override
    public Optional<Task> findById(long id) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()){
                return Optional.ofNullable(TaskRowMapper.mapRow(rs));
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Error while finding user by id.");
        }
    }

    /**
     * Получение списка задач для конкретного пользователя.
     * @param userId идентификатор пользователя.
     * @return список задач
     */
    @Override
    public List<Task> findAllByUserId(long userId) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()){
                return TaskRowMapper.mapRows(rs);
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Error while finding all by user id.");
        }
    }

    /**
     * Связывание задачи с конкретным пользователем.
     * @param taskId идентификатор задачи.
     * @param userId идентификатор пользователя.
     */
    @Override
    public void assignToUserById(long taskId, long userId) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Error while assigning to user.");
        }
    }

    /**
     * Обновление задачи.
     * @param task объект задачи.
     */
    @Override
    public void update(Task task) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());
            if (task.getDescription() == null){
                statement.setNull(2, Types.VARCHAR);
            }else{
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null){
                statement.setNull(3, Types.TIMESTAMP);
            }else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().toString());
            statement.setLong(5, task.getId());
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Error while updating to task.");
        }
    }

    /**
     * Создание новой задачи.
     * @param task объект задачи.
     */
    @Override
    public void create(Task task) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, task.getTitle());
            if (task.getDescription() == null){
                statement.setNull(2, Types.VARCHAR);
            }else{
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null){
                statement.setNull(3, Types.TIMESTAMP);
            }else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().toString());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                task.setId(rs.getLong(1));
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Error while creating to task. Message: " + throwables.getMessage());
        }
    }

    /**
     * Удаление задачи.
     * @param id идентификатор задачи.
     */
    @Override
    public void delete(long id) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Error while deleting to task.");
        }
    }
}
