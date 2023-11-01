package ru.maliutin.tasklist.web.mappers;

import org.mapstruct.Mapper;
import ru.maliutin.tasklist.domain.user.User;
import ru.maliutin.tasklist.web.dto.user.UserDto;

/**
 * Интерфейс реализующий методы преобразования объектов User.
 */

/*
    Аннотация обозначающая, что класс осуществляет преобразование объектов.
    Указываем что класс является компонентом Spring и его можно вызывать используя @Autowired
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Преобразование модели объекта User в объект данных UserDto.
     * @param user объект модели.
     * @return объект данных.
     */
    UserDto toDto(User user);

    /**
     * Преобразование объекта данных UserDto в объект модели User.
     * @param userDto объект данных.
     * @return объект модели.
     */
    User toEntity(UserDto userDto);
}
