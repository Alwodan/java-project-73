package hexlet.code.service.implementations;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.interfaces.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;
    private final TaskRepository taskRepository;

    @Override
    public Label readById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zero labels with such id"));
    }

    @Override
    public List<Label> readAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label create(LabelDto dto) {
        Label label = new Label();
        label.setName(dto.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label update(Long id, LabelDto dto) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Zero labels with such id"));
        label.setName(dto.getName());
        return labelRepository.save(label);
    }

    @Override
    public void delete(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cannot delete non-existing status"));
        if (taskRepository.existsByLabelsId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This label is associated with a task");
        }
        labelRepository.delete(label);
    }
}
