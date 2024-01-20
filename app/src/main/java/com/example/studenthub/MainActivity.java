package com.example.studenthub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

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
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
            }
        }
    }

    private void loadStudentData() {
        SharedPreferences sharedPreferences = getSharedPreferences("StudentPreferences", Context.MODE_PRIVATE);
        int numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

        adapter.clearStudents();

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
}