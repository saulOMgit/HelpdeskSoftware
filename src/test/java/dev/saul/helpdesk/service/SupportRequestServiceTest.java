package dev.saul.helpdesk.service;

import dev.saul.helpdesk.model.RequestStatusEnum;
import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.repository.SupportRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportRequestServiceTest {

    private SupportRequestRepository repository;
    private SupportRequestService service;

    @BeforeEach
    void setUp() {
        repository = mock(SupportRequestRepository.class);
        service = new SupportRequestService(repository);
    }

    @Test
    void testCreateRequest() {
        SupportRequest req = new SupportRequest();
        req.setRequesterName("Ana");

        when(repository.save(any(SupportRequest.class))).thenAnswer(i -> i.getArgument(0));

        SupportRequest created = service.createRequest(req);
        assertEquals(RequestStatusEnum.PENDING, created.getStatus());
        assertNotNull(created.getRequestDate());
        verify(repository, times(1)).save(req);
    }

    @Test
    void testEditRequest() {
        SupportRequest existing = new SupportRequest();
        existing.setId(1L);
        existing.setRequesterName("Ana");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(SupportRequest.class))).thenAnswer(i -> i.getArgument(0));

        SupportRequest updated = service.editRequest(1L, existing);
        assertNotNull(updated.getUpdatedAt());
        verify(repository, times(1)).save(existing);
    }

    @Test
    void testAttendRequest() {
        SupportRequest req = new SupportRequest();
        req.setId(1L);
        req.setStatus(RequestStatusEnum.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(req));
        when(repository.save(any(SupportRequest.class))).thenAnswer(i -> i.getArgument(0));

        SupportRequest attended = service.attendRequest(1L, "Me");
        assertEquals(RequestStatusEnum.ATTENDED, attended.getStatus());
        assertEquals("Me", attended.getAttendedBy());
        assertNotNull(attended.getAttendedAt());
        verify(repository, times(1)).save(req);
    }

    @Test
    void testDeleteRequest_Attended() {
        SupportRequest req = new SupportRequest();
        req.setId(1L);
        req.setStatus(RequestStatusEnum.ATTENDED);

        when(repository.findById(1L)).thenReturn(Optional.of(req));
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.deleteRequest(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRequest_NotAttended() {
        SupportRequest req = new SupportRequest();
        req.setId(1L);
        req.setStatus(RequestStatusEnum.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(req));

        Exception ex = assertThrows(IllegalStateException.class, () -> service.deleteRequest(1L));
        assertEquals("Cannot delete a request that is not attended", ex.getMessage());
    }
}
