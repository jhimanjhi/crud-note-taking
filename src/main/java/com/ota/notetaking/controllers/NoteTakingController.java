package com.ota.notetaking.controllers;

import com.ota.notetaking.models.Note;
import com.ota.notetaking.services.NoteTakingService;
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
    public Mono<Note> createNote(@Valid @RequestBody Note note) {
        return noteService.createNote(note);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Note> getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Note> updateNote(@PathVariable Long id, @Valid @RequestBody Note updatedNote) {
        return noteService.updateNote(id, updatedNote);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<String> deleteNoteById(@PathVariable Long id) {
        return noteService.deleteNoteById(id);
    }

    @DeleteMapping()
    public Mono<String> deleteAllNotes() {
        return noteService.deleteAllNotes();
    }
}
