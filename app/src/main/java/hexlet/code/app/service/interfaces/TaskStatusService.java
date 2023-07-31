package hexlet.code.app.service.interfaces;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;

import java.util.List;

public interface TaskStatusService {
    TaskStatus readById(Long id);

    List<TaskStatus> readAll();

    TaskStatus create(TaskStatusDto dto);

    TaskStatus update(Long id, TaskStatusDto dto);

    void delete(Long id);
}
