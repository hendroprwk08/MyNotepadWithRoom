package com.dicoding.hendro.mynotepadwithroom;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = { Note.class }, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();

    private static NoteDatabase noteDB;

    public static NoteDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static NoteDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
               NoteDatabase.class,
                Constants.DB_NAME)
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        noteDB = null;
    }
}