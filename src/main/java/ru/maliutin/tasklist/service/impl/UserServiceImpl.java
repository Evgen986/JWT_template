package ru.maliutin.tasklist.service.impl;

import org.springframework.stereotype.Service;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.service.UserService;
/**
 * Класс реализующий интерфейс UserService и содержащий бизнес-логику программы.
 * Осуществляет запросы к репозиторию и взаимодействующий с моделью User.
 */
@Service  // Аннотация обозначающая класс как объект сервиса для Spring
public class UserServiceImpl implements UserService {
    @Override
    public User getById(long id) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public boolean isTaskOwner(Long userId, long taskId) {
        return false;
    }

    @Override
    public void delete(long id) {

    }
}
