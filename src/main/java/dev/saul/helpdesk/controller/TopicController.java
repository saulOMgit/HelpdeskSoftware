package dev.saul.helpdesk.controller;

import dev.saul.helpdesk.model.Topic;
import dev.saul.helpdesk.repository.TopicRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicRepository repository;

    public TopicController(TopicRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Topic> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Topic create(@RequestBody Topic topic) {
        return repository.save(topic);
    }
}
