package dev.saul.helpdesk.controller;

import dev.saul.helpdesk.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

class TopicControllerTest {

    private MockMvc mockMvc;
    private TopicRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(TopicRepository.class);
        TopicController controller = new TopicController(repository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetAll() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/topics"))
                .andExpect(status().isOk());
    }
}
