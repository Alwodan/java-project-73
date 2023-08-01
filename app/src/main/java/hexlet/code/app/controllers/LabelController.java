package hexlet.code.app.controllers;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.service.interfaces.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static hexlet.code.app.controllers.LabelController.LABEL_CONTROLLER_PATH;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {

    private final LabelService labelService;

    public static final String LABEL_CONTROLLER_PATH = "/labels";

    @GetMapping("/{id}")
    public Label getLabelById(@PathVariable Long id) {
        return labelService.readById(id);
    }

    @GetMapping("")
    public List<Label> getLabels() {
        return labelService.readAll();
    }

    @PostMapping("")
    public Label createLabel(@RequestBody LabelDto dto) {
        return labelService.create(dto);
    }

    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable Long id, @RequestBody LabelDto dto) {
        return labelService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Long id) {
        labelService.delete(id);
    }
}
