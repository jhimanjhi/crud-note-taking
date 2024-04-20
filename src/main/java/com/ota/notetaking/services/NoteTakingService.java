package com.ota.notetaking.services;

import com.ota.notetaking.models.Note;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteTakingService {
    Mono<Note> createNote(Note note);

    Flux<Note> getAllNotes();

    Mono<Note> getNoteById(Long id);

    Mono<Note> updateNote(Long id, Note updatedNote);

    Mono<String> deleteNoteById(Long id);

    Mono<String> deleteAllNotes();
}
