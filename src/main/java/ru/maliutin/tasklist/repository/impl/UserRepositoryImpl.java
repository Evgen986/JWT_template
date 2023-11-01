package ru.maliutin.tasklist.repository.impl;

import org.springframework.stereotype.Repository;
import ru.maliutin.tasklist.domain.user.Role;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.repository.UserRepository;

import java.util.Optional;

/**
 * Класс реализующий функциональность репозитория объектов пользователя.
 */
@Repository  // Аннотация обозначающая класс как объект репозитория для Spring
public class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> finByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void create(User user) {

    }

    @Override
    public void insertUserRole(long id, Role role) {

    }

    @Override
    public boolean isTaskOwner(long userId, long taskId) {
        return false;
    }

    @Override
    public void delete(long id) {

    }
}
