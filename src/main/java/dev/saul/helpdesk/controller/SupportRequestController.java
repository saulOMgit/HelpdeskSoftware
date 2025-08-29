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

    @PutMapping("/{id}")
    public SupportRequest update(@PathVariable Long id, @RequestBody SupportRequest request) {
        return service.editRequest(id, request);
    }

    @PutMapping("/{id}/attend")
    public SupportRequest attend(@PathVariable Long id, @RequestParam String attendedBy) {
        return service.attendRequest(id, attendedBy);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRequest(id);
    }
}
