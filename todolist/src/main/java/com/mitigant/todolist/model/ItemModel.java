package com.mitigant.todolist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ItemModel implements Serializable {
    @JsonProperty
    private String description;
    @JsonProperty
    private LocalDateTime dueDate;

}
