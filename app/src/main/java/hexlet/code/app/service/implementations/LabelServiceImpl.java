package hexlet.code.app.service.implementations;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.service.interfaces.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;

    @Override
    public Label readById(Long id) {
        return labelRepository.findById(id).orElseThrow();
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
        Label label = labelRepository.findById(id).orElseThrow();
        label.setName(dto.getName());
        return labelRepository.save(label);
    }

    @Override
    public void delete(Long id) {
        Label label = labelRepository.findById(id).orElseThrow();

        labelRepository.delete(label);
    }
}
