package hexlet.code.app.service.interfaces;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;

import java.util.List;

public interface TaskService {
    Task readById(Long id);

    List<Task> readAll();

    Task create(TaskDto dto);

    Task update(Long id, TaskDto dto);

    void delete(Long id);
}
