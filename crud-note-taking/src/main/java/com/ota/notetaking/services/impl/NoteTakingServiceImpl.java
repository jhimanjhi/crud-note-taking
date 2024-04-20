package com.ota.notetaking.services.impl;

import com.ota.notetaking.exceptions.NoteTakingServiceException;
import com.ota.notetaking.models.Note;
import com.ota.notetaking.services.NoteTakingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NoteTakingServiceImpl implements NoteTakingService {

    private final ConcurrentHashMap<Long, Note> notes = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    private static final String NOTE_NOT_FOUND_WITH_ID = "Note not found with id: ";

    @Override
    public Mono<Note> createNote(Note note) {
        long id = idCounter.incrementAndGet();
        note.setId(id);
        note.setCreationDate(LocalDateTime.now());
        notes.put(id, note);
        return Mono.just(note);
    }

    @Override
    public Flux<Note> getAllNotes() {
        return Flux.fromIterable(notes.values());
    }

    @Override
    public Mono<Note> getNoteById(Long id) {
        Note note = notes.get(id);
        if (note != null) {
            return Mono.just(note);
        } else {
            throw new NoteTakingServiceException(HttpStatus.NOT_FOUND, NOTE_NOT_FOUND_WITH_ID + id);
        }
    }

    @Override
    public Mono<Note> updateNote(Long id, Note updatedNote) {
        return Mono.fromCallable(() -> {
            Note existingNote = notes.get(id);
            if (existingNote != null) {
                existingNote.setTitle(updatedNote.getTitle());
                existingNote.setBody(updatedNote.getBody());
                existingNote.setUpdateDate(LocalDateTime.now());
                return existingNote;
            } else {
                throw new NoteTakingServiceException(HttpStatus.NOT_FOUND, "Failed to update. " + NOTE_NOT_FOUND_WITH_ID + id);
            }
        });
    }

    @Override
    public Mono<String> deleteNoteById(Long id) {
        return Mono.fromCallable(() -> {
            boolean removed = notes.remove(id) != null;
            if (removed) {
                return "Success deleting Note with id: " + id;
            } else {
                throw new NoteTakingServiceException(HttpStatus.NOT_FOUND, "Failed to delete. " + NOTE_NOT_FOUND_WITH_ID + id);
            }
        });
    }

    @Override
    public Mono<String> deleteAllNotes() {
        return Mono.fromCallable(() -> {
                notes.clear();
                return "All notes have been cleared.";
        });
    }
}

