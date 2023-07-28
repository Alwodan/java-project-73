package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;

import java.util.List;

public interface UserService {
    User create(UserDto userDto);

    User update(Long id, UserDto userDto);

    List<User> readAll();

    User readById(Long id);

    void delete(Long id);
}
