package com.example.labex7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BookDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "BookTable";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_AUTHOR = "author";
    private static final String COL_GENRE = "genre";
    private static final String COL_YEAR = "year";

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create table
        String sql = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " VARCHAR(255), " +
                COL_AUTHOR + " VARCHAR(255), " +
                COL_GENRE + " VARCHAR(255), " +
                COL_YEAR + " INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertBook(String title, String author, String genre, int year) {
        // Writeable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Content values instance to write into the database
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_AUTHOR, author);
        cv.put(COL_GENRE, genre);
        cv.put(COL_YEAR, year);

        // Insert book into the database
        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    public boolean updateBook(int id, String title, String author, String genre, int year) {
        // Writeable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Content values instance to write into the database
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_AUTHOR, author);
        cv.put(COL_GENRE, genre);
        cv.put(COL_YEAR, year);

        // Update book in the database
        return db.update(TABLE_NAME, cv, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteBook(int id) {
        // Writeable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete book from the database
        return db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getAllBooks() {
        // Readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // Get all books from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getBookById(int id) {
        // Readable database instance
        SQLiteDatabase db = this.getReadableDatabase();

        // Get book from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}
