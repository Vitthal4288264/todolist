package com.mitigant.todolist.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String description;

    private LocalDateTime creationDate = LocalDateTime.now();

    private LocalDateTime dueDate;

    private LocalDateTime lastUpdatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_DONE;
}
