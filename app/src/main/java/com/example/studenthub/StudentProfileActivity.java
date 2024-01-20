package com.example.studenthub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class StudentProfileActivity extends AppCompatActivity {

    TextView displayedName, displayedMatricNo, displayedYear, displayedSemester, displayedMajor, displayedEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        displayedName = findViewById(R.id.studentName);
        displayedMatricNo = findViewById(R.id.matricNo);
        displayedYear = findViewById(R.id.year);
        displayedSemester = findViewById(R.id.semester);
        displayedMajor = findViewById(R.id.major);
        displayedEmail = findViewById(R.id.email);

        if(getIntent().hasExtra("SELECTED_STUDENT")) {
            Student selectedStudent = getIntent().getParcelableExtra("SELECTED_STUDENT");

            displayedName.setText(selectedStudent.getName());
            displayedMatricNo.setText(selectedStudent.getMatricNo());
            displayedYear.setText(String.valueOf(selectedStudent.getYear()));
            displayedSemester.setText(String.valueOf(selectedStudent.getSemester()));
            displayedMajor.setText(selectedStudent.getMajor());
            displayedEmail.setText(selectedStudent.getEmail());
        }
    }
}