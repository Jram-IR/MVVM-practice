package com.jayaram.mvvmpractice;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NoteRepository {
    private final LiveData<List<Note>> allNotes;
    private final DAO_BackgroundAccess  dao_backroundAccess;

    public NoteRepository(Application application)
    {
        NoteDatabase database= NoteDatabase.getInstance(application);
        NoteDao noteDao = database.noteDao();
        allNotes= noteDao.getAllNotes();
        dao_backroundAccess = new DAO_BackgroundAccess(noteDao);


    }

    public void insert(Note note)
    {
        dao_backroundAccess.insertNote(note);
    }

    public void updateNote(Note note)
    {
        dao_backroundAccess.updateNotes(note);

    }

    public void delete(Note note)
    {
        dao_backroundAccess.deleteNotes(note);

    }

    public void deleteAllNotes()
    {
        dao_backroundAccess.deleteAllNote();
    }

    public LiveData<List<Note>> getAllNotes() {
        dao_backroundAccess.getAllNotes();
        return allNotes;
    }



    public static class DAO_BackgroundAccess {
        private final NoteDao noteDao;
        LiveData<List<Note>> allNotes;
        private final ExecutorService executorService = Executors.newSingleThreadExecutor();

        DAO_BackgroundAccess(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        public LiveData<List<Note>> getAllNotes()
        {
            executorService.execute(() -> allNotes=noteDao.getAllNotes());
            return  allNotes;
        }



        public void insertNote(Note note) {
            executorService.execute(() -> noteDao.insert(note));
        }

        public void deleteNotes(Note note)
        {
            executorService.execute(() -> noteDao.delete(note));
        }
        public void updateNotes(Note note)
        {
            executorService.execute(() -> noteDao.update(note));
        }

        public void deleteAllNote()
        {
            executorService.execute(noteDao::deleteAllNotes);
        }
    }


}
