package hexlet.code.service.implementations;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.interfaces.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus readById(Long id) {
        return taskStatusRepository.findById(id).orElseThrow();
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
            throw new IllegalArgumentException("Cannot update non-existing user");
        }
        TaskStatus status = new TaskStatus();
        status.setName(dto.getName());
        status.setId(id);
        return taskStatusRepository.save(status);
    }

    @Override
    public void delete(Long id) {
        TaskStatus user = taskStatusRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot delete non-existing user"));

        taskStatusRepository.delete(user);
    }
}