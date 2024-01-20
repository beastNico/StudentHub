package com.example.studenthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddStudent extends AppCompatActivity {

    EditText name, matricNo, year, semester, major, email;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        name = findViewById(R.id.add_studentName);
        matricNo = findViewById(R.id.add_studentMatric);
        year = findViewById(R.id.add_studentYear);
        semester = findViewById(R.id.add_studentSemester);
        major = findViewById(R.id.add_studentMajor);
        email = findViewById(R.id.add_studentEmail);
        save = findViewById(R.id.button_save_addStudent);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentName = name.getText().toString();
                String studentMatricNo = matricNo.getText().toString();
                int studentYear = Integer.parseInt(year.getText().toString());
                int studentSemester = Integer.parseInt(semester.getText().toString());
                String studentMajor = major.getText().toString();
                String studentEmail = email.getText().toString();

                Student newStudent = new Student(studentName, studentMatricNo, studentYear, studentSemester, studentMajor, studentEmail);

                Intent intent = new Intent(AddStudent.this, MainActivity.class);
                intent.putExtra("NEW_STUDENT", (CharSequence) newStudent);
                startActivity(intent);
            }
        });
    }
}