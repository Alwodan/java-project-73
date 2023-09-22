package hexlet.code.service.implementations;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.interfaces.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private TaskStatusRepository taskStatusRepository;
    private final TaskRepository taskRepository;

    @Override
    public TaskStatus readById(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such task"));
    }

    @Override
    public List<TaskStatus> readAll() {
        return taskStatusRepository.findAll();
    }

    @Override
    public TaskStatus create(TaskStatusDto dto) {
        TaskStatus status = new TaskStatus();
        status.setName(dto.getName());
        return taskStatusRepository.save(status);
    }

    @Override
    public TaskStatus update(Long id, TaskStatusDto dto) {
        if (taskStatusRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update non-existing status");
        }
        TaskStatus status = new TaskStatus();
        status.setName(dto.getName());
        status.setId(id);
        return taskStatusRepository.save(status);
    }

    @Override
    public void delete(Long id) {
        TaskStatus user = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cannot delete non-existing status"));
        if (taskRepository.existsByTaskStatusId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This status is associated with a task");
        }
        taskStatusRepository.delete(user);
    }
}
