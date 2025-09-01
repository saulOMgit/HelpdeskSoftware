package dev.saul.helpdesk.controller;

import dev.saul.helpdesk.model.RequestStatusEnum;
import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.model.Topic;
import dev.saul.helpdesk.service.SupportRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SupportRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SupportRequestService service;

    @InjectMocks
    private SupportRequestController controller;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @Test
    void testGetAll() throws Exception {
        Topic topic = new Topic("Soporte Técnico");
        SupportRequest request = new SupportRequest();
        request.setId(1L);
        request.setRequesterName("Saul");
        request.setDescription("No funciona el login");
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatusEnum.PENDING);
        request.setTopic(topic);

        Mockito.when(service.getAllRequests()).thenReturn(Arrays.asList(request));

        mockMvc.perform(get("/api/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requesterName").value("Saul"))
                .andExpect(jsonPath("$[0].topic.name").value("Soporte Técnico"));
    }

    @Test
    void testCreate() throws Exception {
        Topic topic = new Topic("Soporte Técnico");
        SupportRequest request = new SupportRequest();
        request.setId(1L);
        request.setRequesterName("Saul");
        request.setDescription("No funciona el login");
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatusEnum.PENDING);
        request.setTopic(topic);

        Mockito.when(service.createRequest(any(SupportRequest.class))).thenReturn(request);

        mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requesterName").value("Saul"))
                .andExpect(jsonPath("$.topic.name").value("Soporte Técnico"));
    }

    @Test
    void testUpdate() throws Exception {
        Topic topic = new Topic("Redes");
        SupportRequest updated = new SupportRequest();
        updated.setId(1L);
        updated.setRequesterName("Saul");
        updated.setDescription("Problema con WiFi");
        updated.setRequestDate(LocalDateTime.now());
        updated.setStatus(RequestStatusEnum.PENDING);
        updated.setTopic(topic);

        Mockito.when(service.editRequest(eq(1L), any(SupportRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/requests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Problema con WiFi"))
                .andExpect(jsonPath("$.topic.name").value("Redes"));
    }

    @Test
    void testAttend() throws Exception {
        Topic topic = new Topic("Soporte Técnico");
        SupportRequest attended = new SupportRequest();
        attended.setId(1L);
        attended.setRequesterName("Saul");
        attended.setDescription("No funciona el login");
        attended.setRequestDate(LocalDateTime.now());
        attended.setStatus(RequestStatusEnum.ATTENDED);
        attended.setTopic(topic);
        attended.setAttendedBy("Carlos");

        Mockito.when(service.attendRequest(1L, "Carlos")).thenReturn(attended);

        mockMvc.perform(put("/api/requests/1/attend?attendedBy=Carlos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATTENDED"))
                .andExpect(jsonPath("$.attendedBy").value("Carlos"));
    }

    @Test
    void testDelete() throws Exception {
        Mockito.doNothing().when(service).deleteRequest(1L);

        mockMvc.perform(delete("/api/requests/1"))
                .andExpect(status().isOk());
    }
}
