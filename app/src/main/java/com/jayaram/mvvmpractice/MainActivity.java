package com.jayaram.mvvmpractice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteClickListener {

    private NotesAdapter notesAdapter;
    private NoteViewModel noteViewModel;
    private  static final int  EDIT_NOTE=69;
    private static final String EXTRA_NOTE="package com.jayaram.mvvmpractice.extra_note";
    private static final String EXTRA_EDIT_NOTE="package com.jayaram.mvvmpractice.extra_edit_note";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        //Setting RecyclerView
        RecyclerView notes_RecyclerView = findViewById(R.id.notes_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notes_RecyclerView.setLayoutManager(layoutManager);
        notes_RecyclerView.setHasFixedSize(true);
        notesAdapter=new NotesAdapter(this);
        notes_RecyclerView.setAdapter(notesAdapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> notesAdapter.submitList(notes));

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,MainActivity2.class);
              AddOrEdit_Note_Activity_Intent.launch(intent);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
               0,  ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.RIGHT || direction==ItemTouchHelper.LEFT)
                {
                    Note note = Objects.requireNonNull(noteViewModel.getAllNotes().getValue()).get(viewHolder.getAdapterPosition());
                    noteViewModel.delete(note);
                    Toast.makeText(getApplicationContext(), "delete : "+note.getTitle().trim(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(notes_RecyclerView);


    }

    ActivityResultLauncher<Intent> AddOrEdit_Note_Activity_Intent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK)
                    {
                        assert result.getData() != null;
                        Note new_note = result.getData().getParcelableExtra(EXTRA_NOTE);
                        noteViewModel.insert(new_note);
                    }
                    else if(result.getResultCode()==EDIT_NOTE)
                    {
                        assert result.getData() != null;
                        Note edited_note = result.getData().getParcelableExtra(EXTRA_NOTE);
                        noteViewModel.updateNote(edited_note);

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Note not Saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public void onClick(int position) {
        Note note = Objects.requireNonNull(noteViewModel.getAllNotes().getValue()).get(position);
        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
        intent.putExtra(EXTRA_EDIT_NOTE,note);
        AddOrEdit_Note_Activity_Intent.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete_allNotes) {
            noteViewModel.deleteAllNotes();
            return true;
        }
        else
        return super.onOptionsItemSelected(item);
    }
}