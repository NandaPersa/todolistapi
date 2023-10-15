package com.nandapersa.todolist.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tasks")
@Data
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private UUID idUser;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setDescription(String description) throws Exception {
        if(description.length() > 200) {
            throw new Exception("A descrição precisa ter no máximo 200 caracteres.");
        }
        this.description = description;
    }
}
