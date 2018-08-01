package com.dicoding.hendro.mynotepadwithroom;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 84;
    public static boolean status = false;
    private RecyclerView recyclerView;
    NoteDatabase noteDatabase;
    List<Note> notes;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, NoteActivity.class), REQUEST_CODE);
            }
        });

        notesAdapter = new NotesAdapter (getApplicationContext(), notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);

        noteDatabase = NoteDatabase.getInstance(this);
        new RetrieveTask(this).execute();
    }

    private class RetrieveTask extends AsyncTask<Void,Void,List<Note>> {
        private Context context;

        RetrieveTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return noteDatabase.getNoteDao().getAll(); //output sebagai list
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            notesAdapter.refreshData(notes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            new RetrieveTask(this).execute();
        }
    }
}
