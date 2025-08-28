package dev.saul.helpdesk.service;

import dev.saul.helpdesk.model.RequestStatusEnum;
import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.repository.SupportRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupportRequestService {

    private final SupportRequestRepository repository;

    public SupportRequestService(SupportRequestRepository repository) {
        this.repository = repository;
    }

    public List<SupportRequest> getAllRequests() {
        return repository.findAllByOrderByRequestDateAsc();
    }

    public Optional<SupportRequest> getRequest(Long id) {
        return repository.findById(id);
    }

    public SupportRequest createRequest(SupportRequest request) {
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatusEnum.PENDING);
        return repository.save(request);
    }

    public SupportRequest editRequest(SupportRequest request) {
        request.setUpdatedAt(LocalDateTime.now());
        return repository.save(request);
    }

    public SupportRequest attendRequest(Long id, String attendedBy) {
        SupportRequest req = repository.findById(id).orElseThrow();
        req.setStatus(RequestStatusEnum.ATTENDED);
        req.setAttendedBy(attendedBy);
        req.setAttendedAt(LocalDateTime.now());
        return repository.save(req);
    }

    public void deleteRequest(Long id) {
        SupportRequest req = repository.findById(id).orElseThrow();
        if (req.getStatus() != RequestStatusEnum.ATTENDED) {
            throw new IllegalStateException("Cannot delete a request that is not attended");
        }
        repository.deleteById(id);
    }
}
