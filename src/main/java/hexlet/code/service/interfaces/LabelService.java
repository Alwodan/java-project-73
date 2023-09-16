package hexlet.code.service.interfaces;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {
    Label readById(Long id);

    List<Label> readAll();

    Label create(LabelDto dto);

    Label update(Long id, LabelDto dto);

    void delete(Long id);
}
