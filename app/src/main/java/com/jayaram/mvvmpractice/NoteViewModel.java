package com.jayaram.mvvmpractice;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository=new NoteRepository(application);
        allNotes=repository.getAllNotes();
    }


    public void insert(Note note)
    {
        repository.insert(note);
    }
    public void updateNote(Note note)
    {
        repository.updateNote(note);
    }

    public void delete(Note note)
    {
        repository.delete(note);
    }

    public void deleteAllNotes()
    {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }
}
