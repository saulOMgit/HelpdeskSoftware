package dev.saul.helpdesk.controller;

import dev.saul.helpdesk.model.Topic;
import dev.saul.helpdesk.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Topic> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST (create)
    @PostMapping
    public ResponseEntity<Topic> create(@RequestBody Topic topic) {
        topic.setId(null); // forzar creación
        Topic saved = repository.save(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT (update)
    @PutMapping("/{id}")
    public ResponseEntity<Topic> update(@PathVariable Long id, @RequestBody Topic topic) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(topic.getName());
                    Topic updated = repository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
