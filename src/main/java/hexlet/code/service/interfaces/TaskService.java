package hexlet.code.service.interfaces;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

public interface TaskService {
    Task readById(Long id);

    Iterable<Task> readAll(Predicate predicate);

    Task create(TaskDto dto);

    Task update(Long id, TaskDto dto);

    void delete(Long id);
}
