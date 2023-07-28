package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;

import hexlet.code.config.SpringConfig;
import hexlet.code.utils.TestUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static hexlet.code.config.SpringConfig.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfig.class)
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils utils;

    private final UserDto testDto =
            new UserDto("Andrew", "Tate", "email@gmail.com", "1234");

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    void testWelcomePage() throws Exception {
        MockHttpServletResponse response = utils
                .perform(get("/welcome"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Welcome to Spring");
    }

    @Test
    void testCreatePerson() throws Exception {
        assertEquals(0, userRepository.count());
        utils.regDefaultUser().andExpect(status().isOk());
        assertEquals(1, userRepository.count());
    }

    @Test
    void testGetUser() throws Exception {
        utils.regDefaultUser();
        User expectedUser = userRepository.findByFirstName("Andrew").get();
        MockHttpServletResponse response = utils.perform(
                        get("/users/" + expectedUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        User user = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    public void getAllUsers() throws Exception {
        utils.regDefaultUser();
        final var response = utils.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(users).hasSize(1);
    }

    @Test
    public void updateUser() throws Exception {
        utils.regDefaultUser();

        final Long userId = userRepository.findByFirstName("Andrew").get().getId();

        final var userDto = new UserDto("Going", "Crazy", "jiga@email.ru", "1234");

        final var updateRequest = put("/users/{id}", userId)
                .content(TestUtils.asJson(userDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findByFirstName("Andrew").orElse(null));
        assertNotNull(userRepository.findByFirstName("Going").orElse(null));

    }
}
