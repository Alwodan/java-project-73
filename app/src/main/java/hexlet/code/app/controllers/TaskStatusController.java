package hexlet.code.app.controllers;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.interfaces.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.app.controllers.TaskStatusController.STATUS_CONTROLLER_PATH;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    public static final String STATUS_CONTROLLER_PATH = "/statuses";

    @GetMapping("/{id}")
    public TaskStatus getStatusById(@PathVariable Long id) {
        return taskStatusService.readById(id);
    }

    @GetMapping("")
    public List<TaskStatus> getStatuses() {
        return taskStatusService.readAll();
    }

    @PostMapping("")
    public TaskStatus createStatus(@RequestBody TaskStatusDto dto) {
        return taskStatusService.create(dto);
    }

    @PutMapping("/{id}")
    public TaskStatus updateStatus(@PathVariable Long id, @RequestBody TaskStatusDto dto) {
        return taskStatusService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}
