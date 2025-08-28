package dev.saul.helpdesk.repository;

import dev.saul.helpdesk.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}

