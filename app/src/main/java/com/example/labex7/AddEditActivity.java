package com.example.labex7;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditActivity extends AppCompatActivity {

    private TextView actTitle;
    private EditText title;
    private EditText author;
    private EditText genre;
    private EditText year;
    private Button btn;
    private DbHelper dbHelper;
    private int bookId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the database helper
        dbHelper = new DbHelper(this);

        // Initialize the UI
        actTitle = findViewById(R.id.titleText);
        title = findViewById(R.id.bookTitle);
        author = findViewById(R.id.bookAuthor);
        genre = findViewById(R.id.bookGenre);
        year = findViewById(R.id.bookYear);
        btn = findViewById(R.id.btn);

        // Get the bookId passed from MainActivity or -1 if not available
        bookId = getIntent().getIntExtra("bookId", -1);

        if (bookId == -1) {

            actTitle.setText("Add Book");
            btn.setText("Add");

        } else {

            actTitle.setText("Edit Book");
            btn.setText("Update");

            // Get the book details from the database
            Cursor cursor = dbHelper.getBookById(bookId);
            if (cursor.moveToFirst()) {
                do {
                    int bId = cursor.getInt(0);
                    String bTitle = cursor.getString(1);
                    String bAuthor = cursor.getString(2);
                    String bGenre = cursor.getString(3);
                    int bYear = cursor.getInt(4);

                    // Set the book details to the EditText fields
                    title.setText(bTitle);
                    author.setText(bAuthor);
                    genre.setText(bGenre);
                    year.setText(String.valueOf(bYear));

                } while (cursor.moveToNext());

                cursor.close();
                }
            }


        // On click listener for the button
        btn.setOnClickListener(v -> {
            // call the addBook method
            addBook();
        });



    }

    private void addBook() {
        // Get the values from the EditText fields
        String titleText = title.getText().toString();
        String authorText = author.getText().toString();
        String genreText = genre.getText().toString();

        // Don't parse if the year is empty
        String yearText = year.getText().toString();

        // Validate the input
        boolean isValid = validateInput(titleText, authorText, genreText, yearText);

        if (isValid) {

            if (bookId == -1) {

                dbHelper.insertBook(titleText, authorText, genreText, Integer.parseInt(yearText));

            } else {

                dbHelper.updateBook(bookId, titleText, authorText, genreText, Integer.parseInt(yearText));
            }

            // Clear all the fields
            title.setText("");
            author.setText("");
            genre.setText("");
            year.setText("");

            // Return the user to the main activity
            finish();
        }

    }

    private boolean validateInput(String titleText, String authorText, String genreText, String yearText) {
        // Validate the input and set error messages if necessary
        if (titleText.isEmpty()) {
            title.setError("Title is required");
            return false;
        }

        if (authorText.isEmpty()) {
            author.setError("Author is required");
            return false;
        }

        if (genreText.isEmpty()) {
            genre.setError("Genre is required");
            return false;
        }

        if (yearText.isEmpty()) {
            year.setError("Year is required");
            return false;
        } else {
            try {
                Integer.parseInt(yearText);
            } catch (NumberFormatException e) {
                year.setError("Year must be a number");
                return false;
            }
        }

        return true;
    }
}