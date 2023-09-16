package hexlet.code.service.interfaces;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;

public interface UserService {
    User create(UserDto userDto);

    User update(Long id, UserDto userDto);

    List<User> readAll();

    User readById(Long id);

    void delete(Long id);

    User getCurrentUser();
}
