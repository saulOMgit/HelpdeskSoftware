package dev.saul.helpdesk.controller;

import dev.saul.helpdesk.model.Topic;
import dev.saul.helpdesk.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TopicControllerTest {

    @Mock
    private TopicRepository repository;

    @InjectMocks
    private TopicController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Topic topic = new Topic();
        topic.setName("Test Topic");
        when(repository.findAll()).thenReturn(List.of(topic));

        List<Topic> result = controller.getAll();

        assertEquals(1, result.size());
        assertEquals("Test Topic", result.get(0).getName());
    }

    @Test
    void testGetById_Found() {
        Topic topic = new Topic();
        topic.setName("Topic A");
        when(repository.findById(1L)).thenReturn(Optional.of(topic));

        ResponseEntity<Topic> response = controller.getById(1L);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals("Topic A", response.getBody().getName());
    }

    @Test
    void testGetById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Topic> response = controller.getById(1L);

        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreate() {
        Topic topic = new Topic();
        topic.setName("New Topic");
        Topic saved = new Topic();
        saved.setName("New Topic");

        when(repository.save(topic)).thenReturn(saved);

        ResponseEntity<Topic> response = controller.create(topic);

        assertEquals(org.springframework.http.HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Topic", response.getBody().getName());
        verify(repository, times(1)).save(topic);
    }

    @Test
    void testUpdate_Found() {
        Topic existing = new Topic();
        existing.setName("Old Topic");
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Updated Topic");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        ResponseEntity<Topic> response = controller.update(1L, updatedTopic);

        assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Topic", response.getBody().getName());
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testUpdate_NotFound() {
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Updated Topic");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Topic> response = controller.update(1L, updatedTopic);

        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDelete_Found() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(org.springframework.http.HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = controller.delete(1L);

        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
