package com.dicoding.hendro.mynotepadwithroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private TextInputEditText et_title,et_content;
    private static NoteDatabase noteDatabase;
    private List<Note> notes;
    private Note note;
    NotesAdapter notesAdapter;

    public int EXTRA_POSITION;
    public int EXTRA_ID;
    public String EXTRA_TITLE;
    public String EXTRA_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        et_title = (TextInputEditText) findViewById(R.id.et_title);
        et_content = (TextInputEditText) findViewById(R.id.et_content);
        noteDatabase = NoteDatabase.getInstance(NoteActivity.this);
        Button btSave = findViewById(R.id.but_save);
        Button btDelete = findViewById(R.id.but_delete);

        getSupportActionBar().setSubtitle("Add, Update Or Delete");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        EXTRA_POSITION = intent.getIntExtra("position", 0);
        EXTRA_ID = intent.getIntExtra("id", 0);
        EXTRA_TITLE = intent.getStringExtra("title");
        EXTRA_CONTENT = intent.getStringExtra("content");

        //Toast.makeText(this, EXTRA_POSITION + " " + EXTRA_ID, Toast.LENGTH_LONG ).show();

        et_title.setText(EXTRA_TITLE);
        et_content.setText(EXTRA_CONTENT);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fetch data and create note object
                note = new Note(
                        EXTRA_ID,
                        et_content.getText().toString(),
                        et_title.getText().toString());

                // create worker thread to insert data into database
                new ExecuteTask(note).execute();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(NoteActivity.this);
                confirm.setMessage("Hapus data ini?");
                confirm.setCancelable(true);

                confirm.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                note = new Note(
                                        EXTRA_ID,
                                        et_content.getText().toString(),
                                        et_title.getText().toString());

                                noteDatabase.getNoteDao().delete(note);
                                finish();
                            }
                        });

                confirm.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = confirm.create();
                alert11.show();
            }
        });
    }

    private class ExecuteTask extends AsyncTask<Void,Void,Boolean> {
        private Note note;

        ExecuteTask(Note note) {
            this.note = note;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            //read extra
            if(EXTRA_POSITION == 0){
                noteDatabase.getNoteDao().insert(note);
            }else{
                noteDatabase.getNoteDao().update(note);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent); //By not passing the intent in the result, the calling activity will get null data.
            finish();
        }
    }
}
