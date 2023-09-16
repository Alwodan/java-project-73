package hexlet.code.controllers;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.interfaces.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controllers.TaskStatusController.STATUS_CONTROLLER_PATH;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    public static final String STATUS_CONTROLLER_PATH = "/statuses";

    @Operation(summary = "Get specific task status by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status found"),
            @ApiResponse(responseCode = "404", description = "Task status with that id not found")
    })
    @GetMapping("/{id}")
    public TaskStatus getStatusById(@PathVariable Long id) {
        return taskStatusService.readById(id);
    }

    @Operation(summary = "Get list of all task statuses")
    @ApiResponse(responseCode = "200", description = "List of all task statuses")
    @GetMapping("")
    public List<TaskStatus> getStatuses() {
        return taskStatusService.readAll();
    }


    @Operation(summary = "Create new task status")
    @ApiResponse(responseCode = "201", description = "Task status created")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createStatus(@RequestBody TaskStatusDto dto) {
        return taskStatusService.create(dto);
    }

    @Operation(summary = "Patch task status by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated"),
            @ApiResponse(responseCode = "404", description = "Task status with that id not found")
    })
    @PutMapping("/{id}")
    public TaskStatus updateStatus(@PathVariable Long id, @RequestBody TaskStatusDto dto) {
        return taskStatusService.update(id, dto);
    }

    @Operation(summary = "Delete task status by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status deleted"),
            @ApiResponse(responseCode = "404", description = "Task status with that id not found")
    })
    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable Long id) {
        taskStatusService.delete(id);
    }
}
