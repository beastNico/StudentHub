package com.example.studenthub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private EditText searchBox;
    ImageView addStudent;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addStudent = findViewById(R.id.ic_addStudent);
        recyclerView = findViewById(R.id.recycler_view_students);
        adapter = new StudentAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        searchBox = findViewById(R.id.searchedName);


        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStudent.class);
                startActivityForResult(intent, 1);
            }
        });

        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Student student) {
                Intent profileIntent = new Intent(MainActivity.this, StudentProfileActivity.class);
                profileIntent.putExtra("SELECTED_STUDENT", student);
                startActivity(profileIntent);
            }

            @Override
            public void onDeleteClick(Student student) {
                // Handle delete click
                showDeleteConfirmationDialog(student);
            }
        });

        loadStudentData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("NEW_STUDENT")) {
                Student newStudent = data.getParcelableExtra("NEW_STUDENT");
                adapter.addStudent(newStudent);
            } else if (data.hasExtra("UPDATED_STUDENT")) {
                Student updatedStudent = data.getParcelableExtra("UPDATED_STUDENT");
                adapter.updateStudent(updatedStudent);
            }
        }
    }

    private void loadStudentData() {
        SharedPreferences sharedPreferences = getSharedPreferences("StudentPreferences", Context.MODE_PRIVATE);
        int numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

        for (int i = 1; i <= numberOfStudents; i++) {
            String studentName = sharedPreferences.getString("name" + i, "");
            String studentMatricNo = sharedPreferences.getString("matricNo" + i, "");
            int studentYear = sharedPreferences.getInt("year" + i, -1);
            int studentSemester = sharedPreferences.getInt("semester" + i, -1);
            String studentMajor = sharedPreferences.getString("major" + i, "");
            String studentEmail = sharedPreferences.getString("email" + i, "");

            Student student = new Student(studentName, studentMatricNo, studentYear, studentSemester, studentMajor, studentEmail);
            adapter.addStudent(student);
        }
        adapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Student");
        builder.setMessage("Are you sure you want to delete this student?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the student
                deleteStudent(student);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteStudent(Student student) {
        removeStudentFromSharedPreferences(student);

        adapter.removeStudent(student);
    }

    private void removeStudentFromSharedPreferences(Student student) {
        SharedPreferences sharedPreferences = getSharedPreferences("StudentPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

        // Iterate through the stored students to find the one to be deleted
        for (int i = 1; i <= numberOfStudents; i++) {
            String storedMatricNo = sharedPreferences.getString("matricNo" + i, "");

            // Check if the matricNo matches the one of the student to be deleted
            if (storedMatricNo.equals(student.getMatricNo())) {
                // Remove the data of the matching student
                editor.remove("name" + i);
                editor.remove("matricNo" + i);
                editor.remove("year" + i);
                editor.remove("semester" + i);
                editor.remove("major" + i);
                editor.remove("email" + i);

                // Decrement the number of students
                numberOfStudents--;

                // Shift the data for students after the deleted one
                for (int j = i; j <= numberOfStudents; j++) {
                    editor.putString("name" + j, sharedPreferences.getString("name" + (j + 1), ""));
                    editor.putString("matricNo" + j, sharedPreferences.getString("matricNo" + (j + 1), ""));
                    editor.putInt("year" + j, sharedPreferences.getInt("year" + (j + 1), -1));
                    editor.putInt("semester" + j, sharedPreferences.getInt("semester" + (j + 1), -1));
                    editor.putString("major" + j, sharedPreferences.getString("major" + (j + 1), ""));
                    editor.putString("email" + j, sharedPreferences.getString("email" + (j + 1), ""));
                }

                // Update the number of students in SharedPreferences
                editor.putInt("numberOfStudents", numberOfStudents);

                // Apply the changes
                editor.apply();

                break; // Break out of the loop once the student is deleted
            }
        }
    }
}