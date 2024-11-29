package com.example.coursework;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddClass extends AppCompatActivity {

    private EditText editDate, editTeacher, editComments;
    private Button saveButton, cancelButton;
    private int courseId; // Variable to store courseId
    private Executor executor = Executors.newSingleThreadExecutor();
    private YogaCourseDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Initialize views
        editDate = findViewById(R.id.editDate);
        editTeacher = findViewById(R.id.editTeacher);
        editComments = findViewById(R.id.editComments);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Initialize calendar instance

        // Retrieve courseId from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            courseId = intent.getIntExtra("yogaCourseId", -1); // Default value -1 if not found
        }

        saveButton.setOnClickListener(v -> {
            String date = editDate.getText().toString().trim();
            String teacher = editTeacher.getText().toString().trim();
            String comments = editComments.getText().toString().trim();

            // Check if the required fields are filled
            if (date.isEmpty()) {
                Toast.makeText(AddClass.this, "Please select a date.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (teacher.isEmpty()) {
                Toast.makeText(AddClass.this, "Please enter teacher's name.", Toast.LENGTH_SHORT).show();
                return;
            }

            YogaClass newClass = new YogaClass(0, courseId, date, comments, teacher);
            dbHelper = new YogaCourseDBHelper(this);

            executor.execute(() -> {
                dbHelper.addYogaClass(newClass);  // Add the course to the database

                runOnUiThread(() -> {
                    // Show success message
                    Toast.makeText(AddClass.this, "Yoga class added successfully!", Toast.LENGTH_SHORT).show();

                    // Send result back to MainActivity to reload the data
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();  // Close AddYogaCourseActivity and return to MainActivity
                });
            });

            // You can now use the courseId to create or update the class
            // For example, send the data to a database or backend

            // For now, just show a Toast
            Toast.makeText(AddClass.this, "Class saved successfully for course ID: " + courseId, Toast.LENGTH_SHORT).show();

            // Optionally, finish the activity after saving
            finish();
        });

        // Cancel button click listener
        cancelButton.setOnClickListener(v -> finish());
    }
}
