package ru.maliutin.tasklist.web.mappers;

import org.mapstruct.Mapper;
import ru.maliutin.tasklist.domain.task.Task;
import ru.maliutin.tasklist.web.dto.task.TaskDto;

import java.util.List;

/**
 * Интерфейс реализующий методы преобразования объектов Task.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {
    /**
     * Преобразование модели объекта Task в объект данных taskDto.
     * @param task объект модели.
     * @return объект данных.
     */
    TaskDto toDto(Task task);

    /**
     * Преобразование списка моделей объектов Task в список объектов данных taskDto.
     * @param tasks лист объектов модели.
     * @return лист объектов передачи данных.
     */
    List<TaskDto> toDto(List<Task> tasks);

    /**
     * Преобразование объекта данных TaskDto в объект модели Task.
     * @param taskDto объект данных.
     * @return объект модели.
     */
    Task toEntity(TaskDto taskDto);

}
