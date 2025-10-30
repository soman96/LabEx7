package com.example.labex7;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView booksListView;
    private Button addBookBtn;
    private Button refreshBtn;
    private List<Book> books;
    private DbHelper dbHelper;
    private ArrayAdapter<Book> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup the db
        dbHelper = new DbHelper(this);

        // Initialize the books list and adapter
        books = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, books);

        // Set the listview and button
        booksListView = findViewById(R.id.booksListView);
        addBookBtn = findViewById(R.id.btnAddBook);
        refreshBtn = findViewById(R.id.btnRefresh);

        // Set the onclick listener for the button
        addBookBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);

        // Set long on click listener for the listview
        booksListView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Get the book at the position
            Book book = books.get(position);

            // Call method to create a dialog
            createDialog(book);

            // Return true to indicate that the long click event has been handled
            return true;
        });


        // Set the adapter for the listview
        booksListView.setAdapter(adapter);

        // Populate the books list
        populateBooksList();
    }

    private void createDialog(Book book) {
        // Create a new dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the title and message
        builder
                .setTitle("Manage " + book.getTitle())
                .setMessage("Do you want to edit or delete this book?")
                .setPositiveButton("Edit", (dialog, which) -> {
                    // Intent to call the AddEditActivity with the Book object
                    Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                    intent.putExtra("bookId", book.getId());
                    startActivity(intent);
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    // Call the deleteBook method
                    deleteBook(book);
                });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void deleteBook(Book book) {
        // Delete the book
        dbHelper.deleteBook(book.getId());

        // Remove the book from the books list
        books.remove(book);

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();

        // Show a toast message
        Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show();
    }


    private void populateBooksList() {
        // Clear the books list
        books.clear();

        // Cursor to read all the books
        Cursor cursor = dbHelper.getAllBooks();

        // Move the cursor to the first row
        if (cursor.moveToFirst()) {
            do {
                // Get the values from the cursor
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                String genre = cursor.getString(3);
                int year = cursor.getInt(4);

                // Create a new book object
                Book book = new Book(id, title, author, genre, year);

                // Add the book to the books list
                books.add(book);

            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();

            // Notify the adapter that the data set has changed
            adapter.notifyDataSetChanged();

        } else {
            // Handle the case where there are no books in the database
            Toast.makeText(this, "No books found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btnAddBook) {
            startActivity(new Intent(this, AddEditActivity.class));
        }

        if (id == R.id.btnRefresh) {
            populateBooksList();
            Toast.makeText(this, "Books refreshed", Toast.LENGTH_SHORT).show();
        }
    }
}