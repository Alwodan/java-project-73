package hexlet.code.app.service.implementations;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User readById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No user with such id"));
    }

    public List<User> readAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot delete non-existing user"));

        userRepository.delete(user);
    }

    public User create(UserDto dto) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        return userRepository.save(user);
    }

    public User update(Long id, UserDto dto) {
        if (userRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Cannot update non-existing user");
        }
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .id(id)
                .build();
        return userRepository.save(user);
    }

}
