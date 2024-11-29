package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class YogaCourseDetailActivity extends AppCompatActivity {

    private YogaCourseDBHelper dbHelper;
    private int yogaCourseId;

    private ActivityResultLauncher<Intent> editYogaCourseLauncher;
    private ActivityResultLauncher<Intent> addClassLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_course_detail);

        dbHelper = new YogaCourseDBHelper(this);

        yogaCourseId = getIntent().getIntExtra("yogaCourseId", -1);

        if (yogaCourseId == -1) {
            Toast.makeText(this, "Invalid yoga course ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Registering the result launchers for both activities
        editYogaCourseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadYogaCourseDetails();
                    }
                });

        addClassLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        displayClassesList(); // Update class list after adding a new class
                    }
                });

        // Back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Edit button
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(YogaCourseDetailActivity.this, EditYogaCourseActivity.class);
            intent.putExtra("yogaCourseId", yogaCourseId);
            editYogaCourseLauncher.launch(intent);
        });

        // Delete button
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            boolean isDeleted = dbHelper.deleteYogaCourse(yogaCourseId);
            if (isDeleted) {
                setResult(RESULT_OK);
                Toast.makeText(this, "Yoga course deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete yoga course", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        // Add class button (Fixed listener)
        Button addClass = findViewById(R.id.classAdd);
        addClass.setOnClickListener(v -> {
            Intent intent = new Intent(YogaCourseDetailActivity.this, AddClass.class);
            intent.putExtra("yogaCourseId", yogaCourseId);
            addClassLauncher.launch(intent);
        });
    }

    private void displayClassesList() {
        List<YogaClass> classes = (List<YogaClass>) dbHelper.getAllYogaClass();

        // Check if classes are null or empty
        if (classes == null || classes.isEmpty()) {
            Toast.makeText(this, "No classes available for this course.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ListView setup
        ListView classListView = findViewById(R.id.classListView);

        // You can create an ArrayAdapter for your list of YogaClass objects
        // Assuming you have a simple TextView in the layout for each item in the ListView
        ArrayAdapter<YogaClass> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, classes);

        classListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadYogaCourseDetails();
        displayClassesList();

    }

    private void loadYogaCourseDetails() {
        YogaCourse yogaCourse = dbHelper.getYogaCourseById(yogaCourseId);

        if (yogaCourse == null) {
            Toast.makeText(this, "Yoga course not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Toast.makeText(this, "Yoga hêre found", Toast.LENGTH_SHORT).show();

        TextView classType = findViewById(R.id.classType);
        TextView teacher = findViewById(R.id.teacher);
        TextView dayOfWeek = findViewById(R.id.dayOfWeek);
        TextView time = findViewById(R.id.time);
        TextView capacity = findViewById(R.id.capacity);
        TextView price = findViewById(R.id.price);
        TextView description = findViewById(R.id.description);

        classType.setText(yogaCourse.getClassType());
        teacher.setText(yogaCourse.getTeacher());
        dayOfWeek.setText(yogaCourse.getDayOfWeek());
        time.setText(yogaCourse.getTime());
        capacity.setText(String.valueOf(yogaCourse.getCapacity()));
        price.setText(String.valueOf(yogaCourse.getPrice()));
        description.setText(yogaCourse.getDescription());
    }
}
