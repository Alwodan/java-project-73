package hexlet.code.service.implementations;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.TaskService;
import hexlet.code.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserService userService;
    private UserRepository userRepository;
    private TaskStatusRepository statusRepository;
    private LabelRepository labelRepository;

    @Override
    public Task readById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such task"));
    }

    @Override
    public Iterable<Task> readAll(Predicate predicate) {
        return taskRepository.findAll(predicate);
    }

    @Override
    public Task create(TaskDto dto) {
        Task task = new Task();
        User author = userRepository.findById(userService.getCurrentUser().getId()).orElseThrow();

        task.setAuthor(author);
        return saveDto(task, dto);
    }

    @Override
    public Task update(Long id, TaskDto dto) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot update non-existing task");
        }
        Task task = taskRepository.findById(id).orElseThrow();

        return saveDto(task, dto);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cannot delete non-existing task"));

        taskRepository.delete(task);
    }

    // Maybe I should add something like this to other services
    private Task saveDto(Task task, TaskDto dto) {
        User executor = dto.getExecutorId() == null ? null : userRepository.findById(dto.getExecutorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));

        if (dto.getTaskStatusId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status needed");
        }
        TaskStatus status = statusRepository.findById(dto.getTaskStatusId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such status"));

        List<Label> labels = getLabelsFromIds(dto.getLabelIds());

        task.setExecutor(executor);
        task.setTaskStatus(status);
        task.setLabels(labels);
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());

        return taskRepository.save(task);
    }

    private List<Label> getLabelsFromIds(List<Long> labelIds) {
        List<Label> result = new ArrayList<>();
        if (labelIds != null) {
            for (Long id : labelIds) {
                result.add(labelRepository.findById(id).orElseThrow());
            }
        }
        return result;
    }
}
