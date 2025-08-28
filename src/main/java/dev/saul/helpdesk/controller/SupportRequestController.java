package dev.saul.helpdesk.controller;

import dev.saul.helpdesk.model.SupportRequest;
import dev.saul.helpdesk.service.SupportRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class SupportRequestController {

    private final SupportRequestService service;

    public SupportRequestController(SupportRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<SupportRequest> getAll() {
        return service.getAllRequests();
    }

    @PostMapping
    public SupportRequest create(@RequestBody SupportRequest request) {
        return service.createRequest(request);
    }
}
