package com.ota.notetaking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ota.notetaking.exceptions.CustomExceptionHandler;
import com.ota.notetaking.models.Note;
import com.ota.notetaking.services.impl.NoteTakingServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonbTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class NoteTakingControllerErrorTest {
    @Mock
    private ConcurrentHashMap<Long, Note> notes;

    @InjectMocks
    private NoteTakingServiceImpl noteService;

    private WebTestClient webTestClient;

    private static final Note note1 = Note.builder()
            .id(1L)
            .title("Note 1")
            .body("Title 1")
            .creationDate(LocalDateTime.now())
            .build();

    private static final Note note2 = Note.builder()
            .id(1L)
            .title("Note 1")
            .body("Title 1")
            .creationDate(LocalDateTime.now())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        NoteTakingController controller = new NoteTakingController(noteService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void createNote_FieldValidationError() {

        webTestClient.post()
                .uri("/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"body\": \"test\"}")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getNoteById_NotFoundError() {
        webTestClient.get()
                .uri("/v1/notes/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateNote_NotFoundError() {
        Note updatedNote = Note.builder()
                .title("Updated Title")
                .body("Updated Body")
                .build();

        webTestClient.put()
                .uri("/v1/notes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedNote)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteNoteById_NotFoundError() {

        webTestClient.delete()
                .uri("/v1/notes/1")
                .exchange()
                .expectStatus().isNotFound();
    }
}