package com.example.coursework;


import android.content.Intent;

import android.os.Bundle;
import android.view.TextureView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.android.gms.location.FusedLocationProviderClient;


public class AddYogaCourseActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;

    private EditText dayOfWeekEditText, timeEditText, capacityEditText, durationEditText, priceEditText,
            classTypeEditText, descriptionEditText, teacherEditText;
    private Button saveButton, backButton;
    private YogaCourseDBHelper dbHelper;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextureView textureView;

    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private static final String[] REQUIRED_PERMISSIONS = new String[] {
            "android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_yoga_course);
        dbHelper = new YogaCourseDBHelper(this);

        // Initialize EditText views
        dayOfWeekEditText = findViewById(R.id.dayOfWeekEditText);
        timeEditText = findViewById(R.id.timeEditText);
        capacityEditText = findViewById(R.id.capacityEditText);
        durationEditText = findViewById(R.id.durationEditText);
        priceEditText = findViewById(R.id.priceEditText);
        classTypeEditText = findViewById(R.id.classTypeEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        teacherEditText = findViewById(R.id.teacherEditText);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            try {
                saveYogaCourse();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        // Initialize Back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());  // Close the activity without saving
    }

    private void saveYogaCourse() throws JSONException {
        String dayOfWeek = dayOfWeekEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String capacityStr = capacityEditText.getText().toString();
        String durationStr = durationEditText.getText().toString();
        String priceStr = priceEditText.getText().toString();
        String classType = classTypeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String teacher = teacherEditText.getText().toString();

        if (dayOfWeek.isEmpty() || time.isEmpty() || capacityStr.isEmpty() || durationStr.isEmpty() ||
                priceStr.isEmpty() || classType.isEmpty() || description.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonPayload = new JSONObject();
        try {
            jsonPayload.put("userId", "minh");
            jsonPayload.put("dayOfWeek", dayOfWeek);
            jsonPayload.put("time", time);
            jsonPayload.put("capacity", capacityStr);
            jsonPayload.put("duration", durationStr);
            jsonPayload.put("price", priceStr);
            jsonPayload.put("classType", classType);
            jsonPayload.put("description", description);
            jsonPayload.put("teacher", teacher);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int capacity = 0;
        int duration = 0;
        double price = 0.0;

        try {
            capacity = Integer.parseInt(capacityStr);
            duration = Integer.parseInt(durationStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format. Please enter valid values.", Toast.LENGTH_SHORT).show();
            return;
        }

        YogaCourse newCourse = new YogaCourse(0, dayOfWeek, time, capacity, duration, price, classType, description, teacher);

        // Using ExecutorService to perform the task in background
        executor.execute(() -> {
            dbHelper.addYogaCourse(newCourse);  // Add the course to the database

            runOnUiThread(() -> {
                // Show success message
                Toast.makeText(AddYogaCourseActivity.this, "Yoga class added successfully!", Toast.LENGTH_SHORT).show();

                // Send result back to MainActivity to reload the data
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();  // Close AddYogaCourseActivity and return to MainActivity
            });
        });
    }
}
