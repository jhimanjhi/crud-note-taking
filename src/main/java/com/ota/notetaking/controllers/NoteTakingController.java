package com.ota.notetaking.controllers;

import com.ota.notetaking.models.Note;
import com.ota.notetaking.models.NotePayload;
import com.ota.notetaking.services.NoteTakingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/notes")
public class NoteTakingController {

    private final NoteTakingService noteService;

    public NoteTakingController(NoteTakingService noteService) {
        this.noteService = noteService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Note", description = "API for adding notes.", tags = "A. Creating Notes")
    public Mono<Note> createNote(@Valid @RequestBody NotePayload note) {
        return noteService.createNote(note);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get All Notes", description = "API for fetching all notes.", tags = "B. Fetching Notes")
    public Flux<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Note By ID", description = "API for fetching a specific note.", tags = "B. Fetching Notes")
    public Mono<Note> getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Note By ID", description = "API for updating an existing note." , tags = "C. Updating Notes")
    public Mono<Note> updateNote(@PathVariable Long id, @Valid @RequestBody NotePayload updatedNote) {
        return noteService.updateNote(id, updatedNote);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete Note By ID", description = "API for deleting specific note.", tags = "D. Deleting Notes")
    public Mono<String> deleteNoteById(@PathVariable Long id) {
        return noteService.deleteNoteById(id);
    }

    @DeleteMapping()
    @Operation(summary = "Delete All Notes", description = "API for deleting all notes.", tags = "D. Deleting Notes")
    public Mono<String> deleteAllNotes() {
        return noteService.deleteAllNotes();
    }
}
