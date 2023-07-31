package hexlet.code.app.service.implementations;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.interfaces.TaskService;
import hexlet.code.app.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserService userService;
    private UserRepository userRepository;
    private TaskStatusRepository statusRepository;

    @Override
    public Task readById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such task"));
    }

    @Override
    public List<Task> readAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task create(TaskDto dto) {
        Task task = new Task();
        User author = userRepository.findById(userService.getCurrentUser().getId()).orElseThrow();

        task.setAuthor(author);
        return processDto(task, dto);
    }

    @Override
    public Task update(Long id, TaskDto dto) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Cannot update non-existing task");
        }
        Task task = taskRepository.findById(id).orElseThrow();

        return processDto(task, dto);
    }

    // Maybe I should add something like this to other services
    private Task processDto(Task task, TaskDto dto) {
        User executor =
                dto.getExecutorId() == null ? null : userRepository.findById(dto.getExecutorId()).get();
        TaskStatus status =
                dto.getTaskStatusId() == null ? null : statusRepository.findById(dto.getTaskStatusId()).get();

        task.setExecutor(executor);
        task.setTaskStatus(status);
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());

        return taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot delete non-existing task"));

        taskRepository.delete(task);
    }
}
