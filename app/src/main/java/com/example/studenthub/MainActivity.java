package com.example.studenthub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        recyclerView.setItemAnimator(null);

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
                showDeleteConfirmationDialog(student);
            }
        });

        loadStudentData();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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

        boolean found = false;
        for (int i = 0; i < numberOfStudents; i++) {
            String storedMatricNo = sharedPreferences.getString("matricNo" + i, null);
            if (storedMatricNo != null && storedMatricNo.equals(student.getMatricNo())) {
                found = true; // Mark as found
            }
            if (found) {
                if (i < numberOfStudents - 1) { // Check to prevent IndexOutOfBoundsException
                    editor.putString("name" + i, sharedPreferences.getString("name" + (i + 1), ""));
                    editor.putString("matricNo" + i, sharedPreferences.getString("matricNo" + (i + 1), ""));
                    editor.putInt("year" + i, sharedPreferences.getInt("year" + (i + 1), -1));
                    editor.putInt("semester" + i, sharedPreferences.getInt("semester" + (i + 1), -1));
                    editor.putString("major" + i, sharedPreferences.getString("major" + (i + 1), ""));
                    editor.putString("email" + i, sharedPreferences.getString("email" + (i + 1), ""));
                } else {
                    // Remove the last entry explicitly
                    editor.remove("name" + i);
                    editor.remove("matricNo" + i);
                    editor.remove("year" + i);
                    editor.remove("semester" + i);
                    editor.remove("major" + i);
                    editor.remove("email" + i);
                }
            }
        }
        if (found) {
            // Only decrement numberOfStudents if a deletion occurred
            editor.putInt("numberOfStudents", numberOfStudents - 1);
            editor.apply();
        }
    }


    private void shiftStudentData(SharedPreferences sharedPreferences, int targetIndex, int sourceIndex, SharedPreferences.Editor editor) {
        // Move data from sourceIndex to targetIndex
        editor.putString("name" + targetIndex, sharedPreferences.getString("name" + sourceIndex, ""));
        editor.putString("matricNo" + targetIndex, sharedPreferences.getString("matricNo" + sourceIndex, ""));
        editor.putInt("year" + targetIndex, sharedPreferences.getInt("year" + sourceIndex, -1));
        editor.putInt("semester" + targetIndex, sharedPreferences.getInt("semester" + sourceIndex, -1));
        editor.putString("major" + targetIndex, sharedPreferences.getString("major" + sourceIndex, ""));
        editor.putString("email" + targetIndex, sharedPreferences.getString("email" + sourceIndex, ""));
    }

    private void clearStudentData(int index, SharedPreferences.Editor editor) {
        // Remove data for the student at the given index
        editor.remove("name" + index);
        editor.remove("matricNo" + index);
        editor.remove("year" + index);
        editor.remove("semester" + index);
        editor.remove("major" + index);
        editor.remove("email" + index);
    }

}