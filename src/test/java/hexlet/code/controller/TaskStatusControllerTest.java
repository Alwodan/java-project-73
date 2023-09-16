package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.config.SpringConfig;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.SpringConfig.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.TEST_EMAIL;
import static hexlet.code.utils.TestUtils.TEST_STATUS;
import static hexlet.code.utils.TestUtils.TEST_STATUS2;
import static hexlet.code.utils.TestUtils.asJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static hexlet.code.app.controllers.TaskStatusController.STATUS_CONTROLLER_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfig.class)
public class TaskStatusControllerTest {

    @Autowired
    private TaskStatusRepository statusRepository;

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
    void testCreateStatus() {
        assertEquals(0, statusRepository.count());
        utils.createDefaultStatus();
        assertEquals(1, statusRepository.count());
    }

    @Test
    void testGetStatus() throws Exception {
        utils.createDefaultStatus();

        TaskStatus expectedStatus = statusRepository.findByName(TEST_STATUS).get();
        MockHttpServletResponse response = utils.perform(
                get(STATUS_CONTROLLER_PATH + "/" + expectedStatus.getId()), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        TaskStatus status = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertEquals(expectedStatus.getId(), status.getId());
        assertEquals(expectedStatus.getName(), status.getName());
    }

    @Test
    public void getAllStatuses() throws Exception {
        utils.createDefaultStatus();

        MockHttpServletResponse response = utils.perform(get(STATUS_CONTROLLER_PATH), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> statuses = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(statuses).hasSize(1);
    }

    @Test
    public void updateStatus() throws Exception {
        utils.createDefaultStatus();

        final Long statusId = statusRepository.findByName(TEST_STATUS).get().getId();

        final TaskStatusDto statusDto = new TaskStatusDto(TEST_STATUS2);

        final var updateRequest = put(STATUS_CONTROLLER_PATH + "/" + statusId)
                .content(asJson(statusDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_EMAIL).andExpect(status().isOk());

        assertTrue(statusRepository.existsById(statusId));
        assertNull(statusRepository.findByName(TEST_STATUS).orElse(null));
        assertNotNull(statusRepository.findByName(TEST_STATUS2).orElse(null));
    }

    @Test
    public void deleteStatus() throws Exception {
        utils.createDefaultStatus();

        final Long statusId = statusRepository.findByName(TEST_STATUS).get().getId();

        utils.perform(delete(STATUS_CONTROLLER_PATH + "/" + statusId), TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, statusRepository.count());
    }

}
