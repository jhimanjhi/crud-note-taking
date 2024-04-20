package com.ota.notetaking.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Note {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
}
