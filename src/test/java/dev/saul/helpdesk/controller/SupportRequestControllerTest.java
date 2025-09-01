package dev.saul.helpdesk.controller;

import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.service.SupportRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

class SupportRequestControllerTest {

    private MockMvc mockMvc;
    private SupportRequestService service;

    @BeforeEach
    void setUp() {
        service = mock(SupportRequestService.class);
        SupportRequestController controller = new SupportRequestController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAllRequests()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/requests"))
                .andExpect(status().isOk());
        verify(service, times(1)).getAllRequests();
    }

    @Test
    void testCreate() throws Exception {
        SupportRequest req = new SupportRequest();
        req.setRequesterName("Ana");

        when(service.createRequest(any())).thenReturn(req);

        mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requesterName\": \"Ana\"}"))
                .andExpect(status().isOk());
        verify(service, times(1)).createRequest(any());
    }
}
