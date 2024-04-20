package com.ota.notetaking.controllers;

import com.ota.notetaking.models.Note;
import com.ota.notetaking.services.NoteTakingService;
import com.ota.notetaking.services.impl.NoteTakingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class NoteTakingControllerTest {
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
    void createNote_ValidNote_ReturnsCreated() {
        var createdNote = note1;

        when(notes.put(anyLong(), any())).thenReturn(null);

        webTestClient.post()
                .uri("/v1/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(note1)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Note.class)
                .value(returnedNote -> {
                    assertThat(returnedNote.getId()).isEqualTo(createdNote.getId());
                    assertThat(returnedNote.getTitle()).isEqualTo(createdNote.getTitle());
                    assertThat(returnedNote.getBody()).isEqualTo(createdNote.getBody());
                    assertThat(returnedNote.getCreationDate()).isNotNull();
                });
    }

    @Test
    void getAllNotes_ReturnsListOfNotes() {
        noteService.createNote(note1);
        noteService.createNote(note2);

        webTestClient.get()
                .uri("/v1/notes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Note.class)
                .hasSize(2);
    }

    @Test
    void getNoteById() {
        noteService.createNote(note1);

        webTestClient.get()
                .uri("/v1/notes/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Note.class)
                .value(returnedNote -> {
                    assertThat(returnedNote.getId()).isEqualTo(note1.getId());
                    assertThat(returnedNote.getTitle()).isEqualTo(note1.getTitle());
                    assertThat(returnedNote.getBody()).isEqualTo(note1.getBody());
                });
    }

    @Test
    void updateNote_ValidIdAndNote_ReturnsUpdatedNote() {
        Note updatedNote = Note.builder()
                .title("Updated Title")
                .body("Updated Body")
                .build();

        noteService.createNote(note1);

        webTestClient.put()
                .uri("/v1/notes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedNote)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Note.class)
                .value(returnedNote -> {
                    assertThat(returnedNote.getTitle()).isEqualTo(updatedNote.getTitle());
                    assertThat(returnedNote.getBody()).isEqualTo(updatedNote.getBody());
                    assertThat(returnedNote.getCreationDate()).isNotNull();
                    assertThat(returnedNote.getUpdateDate()).isNotNull();
                });
    }

    @Test
    void deleteNoteById_ValidId() {
        var id = 1L;
        var expectedResponse = "Success deleting Note with id: " + id;

        noteService.createNote(note1);

        webTestClient.delete()
                .uri("/v1/notes/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    void deleteAllNotes() {
        var expectedResponse = "All notes have been cleared.";

        webTestClient.delete()
                .uri("/v1/notes")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expectedResponse);
    }
}