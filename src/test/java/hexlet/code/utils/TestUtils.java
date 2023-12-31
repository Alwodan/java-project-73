package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.UserDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static hexlet.code.controllers.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    public static final String TEST_EMAIL = "andrew@email.com";
    public static final String TEST_EMAIL2 = "tate@email.com";

    public static final String TEST_STATUS = "testingStatus";
    public static final String TEST_STATUS2 = "newStatus";

    public static final String TEST_TASK = "testingTask";
    public static final String TEST_TASK2 = "newTask";

    private final UserDto testRegistrationDto =
            new UserDto(TEST_EMAIL, "Andrew", "Tate", "12345");

    public UserDto getTestRegistrationDto() {
        return testRegistrationDto;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private LabelRepository labelRepository;

    public void tearDown() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        userRepository.deleteAll();
        statusRepository.deleteAll();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testRegistrationDto);
    }

    public ResultActions regUser(final UserDto dto) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

    public void createDefaultStatus() {
        TaskStatus status = new TaskStatus();
        status.setName(TEST_STATUS);
        statusRepository.save(status);
    }

    public void createDefaultTask() {
        createDefaultStatus();
        Task task = Task.builder()
                .name(TEST_TASK)
                .description("testDescription")
                .author(userRepository.findByEmail(TEST_EMAIL).get())
                .taskStatus(statusRepository.findByName(TEST_STATUS).get())
                .build();

        taskRepository.save(task);
    }
}
