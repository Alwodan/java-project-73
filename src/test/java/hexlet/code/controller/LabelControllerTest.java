package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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

import static hexlet.code.config.SpringConfig.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import static hexlet.code.controllers.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.TEST_EMAIL;
import static hexlet.code.utils.TestUtils.asJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfig.class)
public class LabelControllerTest {
    private static final String TEST_LABEL = "testingLabel";
    private static final String TEST_LABEL2 = "newLabel";
    @Autowired
    private LabelRepository labelRepository;

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
    void testCreateLabel() {
        assertEquals(0, labelRepository.count());
        createDefaultLabel();
        assertEquals(1, labelRepository.count());
    }

    @Test
    void testGetLabel() throws Exception {
        createDefaultLabel();

        Label expectedLabel = labelRepository.findByName(TEST_LABEL).get();
        MockHttpServletResponse response = utils.perform(
                get(LABEL_CONTROLLER_PATH + "/" + expectedLabel.getId()), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Label label = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertEquals(expectedLabel.getId(), label.getId());
        assertEquals(expectedLabel.getName(), label.getName());
    }

    @Test
    public void getAllLabels() throws Exception {
        createDefaultLabel();

        MockHttpServletResponse response = utils.perform(get(LABEL_CONTROLLER_PATH), TEST_EMAIL)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<Label> labels = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(labels).hasSize(1);
    }

    @Test
    public void updateLabel() throws Exception {
        createDefaultLabel();

        Long labelId = labelRepository.findByName(TEST_LABEL).get().getId();

        LabelDto dto = new LabelDto(TEST_LABEL2);

        final var request = put(LABEL_CONTROLLER_PATH + "/" + labelId)
                .content(asJson(dto))
                .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request, TEST_EMAIL).andExpect(status().isOk());

        assertTrue(labelRepository.existsById(labelId));
        assertNull(labelRepository.findByName(TEST_LABEL).orElse(null));
        assertNotNull(labelRepository.findByName(TEST_LABEL2).orElse(null));
    }

    @Test
    public void deleteLabel() throws Exception {
        createDefaultLabel();
        Long labelid = labelRepository.findByName(TEST_LABEL).get().getId();

        utils.perform(delete(LABEL_CONTROLLER_PATH + "/" + labelid), TEST_EMAIL)
                .andExpect(status().isOk());

        assertEquals(0, labelRepository.count());
    }

    private void createDefaultLabel() {
        Label label = new Label();
        label.setName(TEST_LABEL);
        labelRepository.save(label);
    }
}
