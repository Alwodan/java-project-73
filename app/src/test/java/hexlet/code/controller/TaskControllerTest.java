package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.config.SpringConfig;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.app.controllers.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.config.SpringConfig.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.TEST_EMAIL;
import static hexlet.code.utils.TestUtils.TEST_STATUS;
import static hexlet.code.utils.TestUtils.TEST_TASK;
import static hexlet.code.utils.TestUtils.TEST_TASK2;
import static hexlet.code.utils.TestUtils.asJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfig.class)
public class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void before() throws Exception {
        utils.regDefaultUser();
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    void testCreateTask() {
        assertEquals(0, taskRepository.count());
        utils.createDefaultTask();
        assertEquals(1, taskRepository.count());
    }

    @Test
    void testGetTask() throws Exception {
        utils.createDefaultTask();

        Task expectedTask = taskRepository.findByName(TEST_TASK).get();
        MockHttpServletResponse response = utils.perform(
                        get(TASK_CONTROLLER_PATH + "/" + expectedTask.getId()), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Task task = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertEquals(expectedTask.getId(), task.getId());
        assertEquals(expectedTask.getName(), task.getName());
    }

    @Test
    public void getAllTasks() throws Exception {
        utils.createDefaultTask();

        MockHttpServletResponse response = utils.perform(get(TASK_CONTROLLER_PATH), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(tasks).hasSize(1);
    }

    @Test
    public void updateTask() throws Exception {
        utils.createDefaultTask();

        final Long taskId = taskRepository.findByName(TEST_TASK).get().getId();

        final TaskDto taskDto = new TaskDto();
        taskDto.setName(TEST_TASK2);
        taskDto.setTaskStatusId(taskStatusRepository.findByName(TEST_STATUS).get().getId());

        final var updateRequest = put(TASK_CONTROLLER_PATH + "/" + taskId)
                .content(asJson(taskDto))
                .contentType(MediaType.APPLICATION_JSON);

        utils.perform(updateRequest, TEST_EMAIL).andExpect(status().isOk());

        assertTrue(taskRepository.existsById(taskId));
        assertNull(taskRepository.findByName(TEST_TASK).orElse(null));
        assertNotNull(taskRepository.findByName(TEST_TASK2).orElse(null));
    }

    @Test
    public void deleteTask() throws Exception {
        utils.createDefaultTask();

        final Long taskId = taskRepository.findByName(TEST_TASK).get().getId();

        utils.perform(delete(TASK_CONTROLLER_PATH + "/" + taskId), TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, taskRepository.count());
    }

}
