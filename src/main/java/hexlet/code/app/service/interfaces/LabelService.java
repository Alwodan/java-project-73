package hexlet.code.app.service.interfaces;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;

import java.util.List;

public interface LabelService {
    Label readById(Long id);

    List<Label> readAll();

    Label create(LabelDto dto);

    Label update(Long id, LabelDto dto);

    void delete(Long id);
}
