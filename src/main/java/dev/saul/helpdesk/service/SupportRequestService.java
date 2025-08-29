package dev.saul.helpdesk.service;

import dev.saul.helpdesk.model.RequestStatusEnum;
import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.repository.SupportRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupportRequestService {

    private final SupportRequestRepository repository;

    public SupportRequestService(SupportRequestRepository repository) {
        this.repository = repository;
    }

    public List<SupportRequest> getAllRequests() {
        return repository.findAllByOrderByRequestDateAsc();
    }

    public SupportRequest getRequest(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + id + " not found"));
    }

    public SupportRequest createRequest(SupportRequest request) {
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatusEnum.PENDING);
        return repository.save(request);
    }

    public SupportRequest editRequest(Long id, SupportRequest newData) {
        SupportRequest req = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        req.setDescription(newData.getDescription());
        req.setTopic(newData.getTopic());
        req.setUpdatedAt(LocalDateTime.now());

        return repository.save(req);
    }

    public SupportRequest attendRequest(Long id, String attendedBy) {
        SupportRequest req = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        req.setStatus(RequestStatusEnum.ATTENDED);
        req.setAttendedBy(attendedBy);
        req.setAttendedAt(LocalDateTime.now());

        return repository.save(req);
    }

    public void deleteRequest(Long id) {
        SupportRequest req = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (req.getStatus() != RequestStatusEnum.ATTENDED) {
            throw new IllegalStateException("Request must be attended before deletion");
        }

        repository.deleteById(id);
    }
}

