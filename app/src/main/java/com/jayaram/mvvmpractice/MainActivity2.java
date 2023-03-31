package com.jayaram.mvvmpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {
    private EditText edt_Title;
    private EditText edt_Description;
  private NumberPicker numberPicker;
  private  static final int  EDIT_NOTE=69;
  private int EDIT_NOTE_ID;
  private static final String EXTRA_NOTE="package com.jayaram.mvvmpractice.extra_note";
    private static final String EXTRA_EDIT_NOTE="package com.jayaram.mvvmpractice.extra_edit_note";
  private Note edit_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        edt_Title=findViewById(R.id.et_title);
        edt_Description=findViewById(R.id.et_description);
        numberPicker=findViewById(R.id.num_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        setTitle("Add Note");
         edit_note = getIntent().getParcelableExtra(EXTRA_EDIT_NOTE);
        if(edit_note!=null)
        {
            setTitle("Edit Note");
            edt_Title.setText(edit_note.getTitle());
            edt_Description.setText(edit_note.getDescription());
            numberPicker.setValue(edit_note.getPriority());
            EDIT_NOTE_ID=edit_note.getId();
        }

    }
    private void saveNote() {
        String title = edt_Title.getText().toString();
        String description = edt_Description.getText().toString();
        String  priority= String.valueOf(numberPicker.getValue());
        Note note = new Note(title,description,Integer.parseInt(priority));
        if(title.trim().isEmpty()||description.trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please Enter the Title OR Description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NOTE,note);
        if(edit_note!=null) {
            setResult(EDIT_NOTE, data);
            note.setId(EDIT_NOTE_ID);
        }
        else
        {
            setResult(RESULT_OK,data);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.save_note) {
            saveNote();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);

    }


}