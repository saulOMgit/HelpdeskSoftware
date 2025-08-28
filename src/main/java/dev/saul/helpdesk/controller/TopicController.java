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

    // GET all
    @GetMapping
    public List<Topic> getAll() {
        return repository.findAll();
    }

    // GET by id
    @GetMapping("/{id}")
    public Topic getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + id));
    }

    // POST (create)
    @PostMapping
    public Topic create(@RequestBody Topic topic) {
        topic.setId(null); // forzar creación
        return repository.save(topic);
    }

    // PUT (update)
    @PutMapping("/{id}")
    public Topic update(@PathVariable Long id, @RequestBody Topic topic) {
        Topic existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id " + id));
        existing.setName(topic.getName());
        return repository.save(existing);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Topic not found with id " + id);
        }
        repository.deleteById(id);
    }
}
