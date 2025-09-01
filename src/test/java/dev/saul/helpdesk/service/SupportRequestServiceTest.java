package dev.saul.helpdesk.service;

import dev.saul.helpdesk.model.RequestStatusEnum;
import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.model.Topic;
import dev.saul.helpdesk.repository.SupportRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SupportRequestServiceTest {

    private SupportRequestRepository repository;
    private SupportRequestService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(SupportRequestRepository.class);
        service = new SupportRequestService(repository);
    }

    @Test
    void testCreateRequest() {
        SupportRequest request = new SupportRequest();
        request.setRequesterName("Ana");
        request.setDescription("Correo no funciona");
        request.setTopic(new Topic("Soporte"));

        when(repository.save(any(SupportRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        SupportRequest saved = service.createRequest(request);

        assertNotNull(saved.getRequestDate());
        assertEquals(RequestStatusEnum.PENDING, saved.getStatus());
        assertEquals("Ana", saved.getRequesterName());
        verify(repository, times(1)).save(request);
    }

    @Test
    void testGetAllRequests() {
        SupportRequest r1 = new SupportRequest();
        SupportRequest r2 = new SupportRequest();
        List<SupportRequest> list = Arrays.asList(r1, r2);

        when(repository.findAllByOrderByRequestDateAsc()).thenReturn(list);

        List<SupportRequest> result = service.getAllRequests();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAllByOrderByRequestDateAsc();
    }

    @Test
    void testGetRequest() {
        SupportRequest request = new SupportRequest();
        request.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(request));

        SupportRequest result = service.getRequest(1L);

        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetRequest_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.getRequest(1L));
        assertEquals("Request with id 1 not found", ex.getMessage());
    }

    @Test
    void testEditRequest() {
        SupportRequest existing = new SupportRequest();
        existing.setId(1L);
        existing.setDescription("Error login");
        existing.setTopic(new Topic("Soporte"));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SupportRequest newData = new SupportRequest();
        newData.setDescription("Error login actualizado");
        newData.setTopic(new Topic("Base de datos"));

        SupportRequest edited = service.editRequest(1L, newData);

        assertEquals("Error login actualizado", edited.getDescription());
        assertEquals("Base de datos", edited.getTopic().getName());
        assertNotNull(edited.getUpdatedAt());
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testEditRequest_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        SupportRequest newData = new SupportRequest();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.editRequest(1L, newData));
        assertEquals("Request not found", ex.getMessage());
    }

    @Test
    void testAttendRequest() {
        SupportRequest existing = new SupportRequest();
        existing.setId(2L);
        existing.setStatus(RequestStatusEnum.PENDING);
        when(repository.findById(2L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SupportRequest attended = service.attendRequest(2L, "Carlos");

        assertEquals(RequestStatusEnum.ATTENDED, attended.getStatus());
        assertEquals("Carlos", attended.getAttendedBy());
        assertNotNull(attended.getAttendedAt());
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testDeleteRequest_Attended() {
        SupportRequest existing = new SupportRequest();
        existing.setId(3L);
        existing.setStatus(RequestStatusEnum.ATTENDED);
        when(repository.findById(3L)).thenReturn(Optional.of(existing));

        service.deleteRequest(3L);

        verify(repository, times(1)).deleteById(3L);
    }

    @Test
    void testDeleteRequest_NotAttended() {
        SupportRequest existing = new SupportRequest();
        existing.setId(4L);
        existing.setStatus(RequestStatusEnum.PENDING);
        when(repository.findById(4L)).thenReturn(Optional.of(existing));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.deleteRequest(4L));

        assertEquals("Request must be attended before deletion", ex.getMessage());
        verify(repository, never()).deleteById(4L);
    }
}
