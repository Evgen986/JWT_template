package ru.maliutin.tasklist.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.maliutin.tasklist.domain.exception.ResourceMappingException;
import ru.maliutin.tasklist.domain.exception.ResourceNotFoundException;
import ru.maliutin.tasklist.domain.user.Role;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.repository.DataSourceConfig;
import ru.maliutin.tasklist.repository.UserRepository;
import ru.maliutin.tasklist.repository.mappers.UserRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Класс реализующий функциональность репозитория объектов пользователя.
 */
@Repository  // Аннотация обозначающая класс как объект репозитория для Spring
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    // Фабрика соединений с БД.
    private final DataSourceConfig dataSourceConfig;

    // SQL запросы
    private final String FIND_BY_ID = """
            SELECT u.id              AS user_id,
                   u.name            AS user_name,
                   u.username        AS user_username,
                   u.password        AS user_password,
                   ur.role           AS user_role_role,
                   t.id              AS task_id,
                   t.title           AS task_title,
                   t.description     AS task_description,
                   t.expiration_date AS task_expiration_date,
                   t.status          AS task_status
            FROM users AS u
                     LEFT JOIN users_roles ur on u.id = ur.user_id
                     LEFT JOIN users_tasks ut on u.id = ut.user_id
                     left join tasks t on t.id = ut.task_id
            WHERE u.id = ?""";
    private final String FIND_BY_USER_NAME = """
            SELECT u.id              AS user_id,
                   u.name            AS user_name,
                   u.username        AS user_username,
                   u.password        AS user_password,
                   ur.role           AS user_role_role,
                   t.id              AS task_id,
                   t.title           AS task_title,
                   t.description     AS task_description,
                   t.expiration_date AS task_expiration_date,
                   t.status          AS task_status
            FROM users AS u
                     LEFT JOIN users_roles ur on u.id = ur.user_id
                     LEFT JOIN users_tasks ut on u.id = ut.user_id
                     left join tasks t on t.id = ut.task_id
            WHERE u.username = ?;""";
    private final String UPDATE = """
            UPDATE users
            SET name     = ?,
                username = ?,
                password = ?
            WHERE id = ?;""";
    private final String CREATE = """
            INSERT INTO users (name, username, password)
            VALUES (?, ?, ?)""";
    private final String INSERT_USER_ROLE = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?, ?)""";
    private final String IS_TASK_OWNER = """
            SELECT exists(SELECT 1
                          FROM users_tasks
                          WHERE user_id = ?
                            AND task_id = ?)""";
    private final String DELETE = """
            DELETE
            FROM users
            WHERE id = ?""";

    /**
     * Получение пользователя по идентификатору.
     * @param id идентификатор пользователя.
     * @throws IllegalStateException исключение при поиске пользователя.
     * @return объект Optional, который может содержать пользователя или null.
     */
    @Override
    public Optional<User> findById(long id) throws IllegalArgumentException {
        try{
            Connection connection = dataSourceConfig.getConnection();
            /* TYPE_SCROLL_INSENSITIVE и ResultSet.CONCUR_READ_ONLY используются
                для возможности прохождения по ResultSet несколько раз, сначала получить роли пользователя,
                потом получить задачи пользователя и в конце самого пользователя.
             */
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try(ResultSet rs = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
                }
            }catch (SQLException throwables){
                throw new IllegalStateException("Exception while finding user by id.");
        }
    }

    /**
     * Получение пользователя по логину.
     * @param username логин пользователя.
     * @throws ResourceMappingException исключение при получении пользователя.
     * @return объект Optional, который может содержать пользователя или null.
     */
    @Override
    public Optional<User> findByUsername(String username) throws ResourceMappingException{
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_NAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, username);
            try(ResultSet rs = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while finding user by username.");
        }
    }

    /**
     * Обновление данных о пользователе в БД.
     * @param user объект пользователя.
     * @throws ResourceMappingException исключение при попытке обновления пользователя.
     */
    @Override
    public void update(User user) throws ResourceMappingException{
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    /**
     * Сохранение нового пользователя в БД.
     * @param user объект пользователя.
     * @throws ResourceMappingException исключение при попытке создания пользователя.
     */
    @Override
    public void create(User user) throws ResourceMappingException{
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                user.setId(rs.getLong(1));
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while creating user.");
        }
    }

    /**
     * Добавление роли пользователя.
     * @param userId идентификатор пользователя.
     * @param role роль пользователя.
     * @throws ResourceMappingException исключение при добавлении роли пользователя.
     */
    @Override
    public void insertUserRole(long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while inserting user role.");
        }
    }

    /**
     * Проверка, принадлежит ли задача пользователю.
     * @param userId идентификатор пользователя.
     * @param taskId идентификатор задачи.
     * @throws ResourceMappingException ошибка при проверке принадлежности задачи пользователю.
     * @return true - если принадлежит, иначе false.
     */
    @Override
    public boolean isTaskOwner(long userId, long taskId) throws ResourceMappingException{
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_TASK_OWNER);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet rs = statement.executeQuery()){
                rs.next();
                return rs.getBoolean(1);
            }
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while checking if user is task owner.");
        }
    }

    /**
     * Удаление пользователя из БД.
     * @throws ResourceMappingException ошибка при удалении пользователя.
     * @param id идентификатор пользователя.
     */
    @Override
    public void delete(long id) throws ResourceMappingException{
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        }catch (SQLException throwables){
            throw new ResourceMappingException("Exception while deleting user.");
        }
    }
}
