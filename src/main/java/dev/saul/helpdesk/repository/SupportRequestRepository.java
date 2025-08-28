package dev.saul.helpdesk.repository;

import dev.saul.helpdesk.model.RequestStatusEnum;
import dev.saul.helpdesk.model.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {
    List<SupportRequest> findAllByOrderByRequestDateAsc();
    List<SupportRequest> findByStatus(RequestStatusEnum status);
}
