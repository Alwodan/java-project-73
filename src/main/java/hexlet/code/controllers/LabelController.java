package hexlet.code.controllers;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.service.interfaces.LabelService;
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

import java.util.List;

import static hexlet.code.controllers.LabelController.LABEL_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {

    private final LabelService labelService;

    public static final String LABEL_CONTROLLER_PATH = "/labels";

    @Operation(summary = "Get specific label by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label found"),
        @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @GetMapping("/{id}")
    public Label getLabelById(@PathVariable Long id) {
        return labelService.readById(id);
    }

    @Operation(summary = "Get list of all labels")
    @ApiResponse(responseCode = "200", description = "List of all labels")
    @GetMapping("")
    public List<Label> getLabels() {
        return labelService.readAll();
    }

    @Operation(summary = "Create new label")
    @ApiResponse(responseCode = "201", description = "Label created")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(@RequestBody LabelDto dto) {
        return labelService.create(dto);
    }

    @Operation(summary = "Patch label by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label updated"),
        @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable Long id, @RequestBody LabelDto dto) {
        return labelService.update(id, dto);
    }

    @Operation(summary = "Delete label by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label deleted"),
        @ApiResponse(responseCode = "404", description = "Label with that id not found"),
        @ApiResponse(responseCode = "409", description = "Label with that id cannot be deleted")
    })
    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Long id) {
        labelService.delete(id);
    }
}
