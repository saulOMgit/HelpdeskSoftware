package dev.saul.helpdesk.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.saul.helpdesk.model.RequestStatusEnum;
import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.model.Topic;
import dev.saul.helpdesk.repository.SupportRequestRepository;
import dev.saul.helpdesk.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SupportRequestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupportRequestRepository supportRequestRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Topic savedTopic;

    @BeforeEach
    void setup() {
        // Primero borramos requests, luego topics para no romper FK
        supportRequestRepository.deleteAll();
        topicRepository.deleteAll();

        savedTopic = topicRepository.save(new Topic(null));
    }

    @Test
    void testCreateAndGetRequest() throws Exception {
        SupportRequest request = new SupportRequest();
        request.setRequesterName("Saul");
        request.setDescription("No funciona el sistema");
        request.setTopic(savedTopic);

        mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Ajustamos al 200 que devuelve tu controller
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value(RequestStatusEnum.PENDING.toString()));
    }

    @Test
    void testAttendRequest() throws Exception {
        SupportRequest request = new SupportRequest();
        request.setRequesterName("Saul");
        request.setDescription("Problema con login");
        request.setTopic(savedTopic);

        SupportRequest saved = supportRequestRepository.save(request);

        mockMvc.perform(put("/api/requests/" + saved.getId() + "/attend")
                        .param("attendedBy", "Admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(RequestStatusEnum.ATTENDED.toString()))
                .andExpect(jsonPath("$.attendedBy").value("Admin"));
    }

    @Test
    void testDeleteAttendedRequest() throws Exception {
        SupportRequest request = new SupportRequest();
        request.setRequesterName("Saul");
        request.setDescription("Eliminar test");
        request.setTopic(savedTopic);
        request.setStatus(RequestStatusEnum.ATTENDED);

        SupportRequest saved = supportRequestRepository.save(request);

        mockMvc.perform(delete("/api/requests/" + saved.getId()))
                // Ajustamos al 200 que devuelve tu controller
                .andExpect(status().isOk());

        Optional<SupportRequest> deleted = supportRequestRepository.findById(saved.getId());
        assertThat(deleted).isEmpty();
    }
}
