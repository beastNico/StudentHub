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
    private CustomArrayList<Student> studentList;

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

        studentList = new CustomArrayList<>();

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

        // Searching logics
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("NEW_STUDENT")) {
                Student newStudent = data.getParcelableExtra("NEW_STUDENT");
                studentList.add(newStudent);
                sortStudentListByName();
                adapter.setStudentList(studentList);
            } else if (data.hasExtra("UPDATED_STUDENT")) {
                Student updatedStudent = data.getParcelableExtra("UPDATED_STUDENT");
                updateStudentInList(updatedStudent);
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
            studentList.add(student);
        }

        sortStudentListByName();
        adapter.setStudentList(studentList);
    }

    private void showDeleteConfirmationDialog(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this student?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the student
                deleteStudent(student);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteStudent(Student student) {
        int index = studentList.indexOf(student);

        if (index >= 0) { // Check if the student is found in the list
            studentList.remove(index); // Remove the student using the index
            removeStudentFromSharedPreferences(student.getMatricNo()); // Remove from SharedPreferences
            adapter.setStudentList(studentList);
            adapter.notifyDataSetChanged(); // Notify adapter of the change
        }
    }

    private void removeStudentFromSharedPreferences(String matriculationNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("StudentPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

        for (int i = 1; i <= numberOfStudents; i++) {
            String storedMatricNo = sharedPreferences.getString("matricNo" + i, "");
            if (storedMatricNo.equals(matriculationNumber)) {
                // Remove the data for the current student
                clearStudentData(i, editor);

                // Shifting data for each subsequent student if necessary
                for (int j = i + 1; j <= numberOfStudents; j++) {
                    shiftStudentData(sharedPreferences, j, j - 1, editor);
                }

                // Decrement numberOfStudents
                editor.putInt("numberOfStudents", numberOfStudents - 1);
                editor.apply();
                break;
            }
        }
    }


    private void shiftStudentData(SharedPreferences sharedPreferences, int targetIndex, int sourceIndex, SharedPreferences.Editor editor) {
        editor.putString("name" + targetIndex, sharedPreferences.getString("name" + sourceIndex, ""));
        editor.putString("matricNo" + targetIndex, sharedPreferences.getString("matricNo" + sourceIndex, ""));
        editor.putInt("year" + targetIndex, sharedPreferences.getInt("year" + sourceIndex, -1));
        editor.putInt("semester" + targetIndex, sharedPreferences.getInt("semester" + sourceIndex, -1));
        editor.putString("major" + targetIndex, sharedPreferences.getString("major" + sourceIndex, ""));
        editor.putString("email" + targetIndex, sharedPreferences.getString("email" + sourceIndex, ""));
    }

    private void clearStudentData(int index, SharedPreferences.Editor editor) {
        editor.remove("name" + index);
        editor.remove("matricNo" + index);
        editor.remove("year" + index);
        editor.remove("semester" + index);
        editor.remove("major" + index);
        editor.remove("email" + index);
    }

    // Bubble Sort
    private void sortStudentListByName() {
        for (int i = 0; i < studentList.size() - 1; i++) {
            for (int j = i + 1; j < studentList.size(); j++) {
                String name1 = studentList.get(i).getName();
                String name2 = studentList.get(j).getName();

                // Extracting the first word from each name
                String[] name1Words = name1.split(" ");
                String[] name2Words = name2.split(" ");

                // Comparing the first characters of the first words
                char firstCharName1 = name1Words[0].charAt(0);
                char firstCharName2 = name2Words[0].charAt(0);

                // If the first characters are different, swap the elements
                if (firstCharName1 > firstCharName2) {

                    Student temp = studentList.get(i);
                    studentList.set(i, studentList.get(j));
                    studentList.set(j, temp);
                }
            }
        }
    }

    private void updateStudentInList(Student updatedStudent) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getMatricNo().equals(updatedStudent.getMatricNo())) {
                studentList.set(i, updatedStudent);
                sortStudentListByName();
                adapter.setStudentList(studentList);
                break;
            }
        }
    }
}
