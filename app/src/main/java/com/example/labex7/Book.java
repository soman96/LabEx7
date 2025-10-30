package com.example.labex7;

import androidx.annotation.NonNull;

public class Book {

    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;

    public Book(int id, String title, String author, String genre, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    @NonNull
    @Override
    public String toString() {
        return id + ": " + title + " by " + author + ", " + genre + ", " + year;
    }

}
