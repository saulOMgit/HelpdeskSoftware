package dev.saul.helpdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterName;        // nombre del solicitante
    private LocalDateTime requestDate;   // fecha de solicitud
    private String description;          // descripción de la consulta

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;                 // relación con tema

    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status;        // PENDIENTE / ATENDIDA

    private String attendedBy;           // quién atendió
    private LocalDateTime attendedAt;    // fecha asistencia
    private LocalDateTime updatedAt;     // fecha edición
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRequesterName() {
        return requesterName;
    }
    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Topic getTopic() {
        return topic;
    }
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public String getAttendedBy() {
        return attendedBy;
    }
    public void setAttendedBy(String attendedBy) {
        this.attendedBy = attendedBy;
    }
    public LocalDateTime getAttendedAt() {
        return attendedAt;
    }
    public void setAttendedAt(LocalDateTime attendedAt) {
        this.attendedAt = attendedAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public RequestStatusEnum getStatus() {
        return status;
    }
    public void setStatus(RequestStatusEnum status) {
        this.status = status;
    }

    
    
}
