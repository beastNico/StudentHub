package com.example.studenthub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

public class StudentProfileActivity extends AppCompatActivity {

    TextView displayedName, displayedMatricNo, displayedYear, displayedSemester, displayedMajor, displayedEmail;
    ImageView icEdit;

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
        icEdit = findViewById(R.id.ic_edit); // Initialize icEdit

        if(getIntent().hasExtra("SELECTED_STUDENT")) {
            Student selectedStudent = getIntent().getParcelableExtra("SELECTED_STUDENT");

            displayedName.setText("Student Name: " + selectedStudent.getName());
            displayedMatricNo.setText("Matric No.: " + selectedStudent.getMatricNo());
            displayedYear.setText("Year: " + String.valueOf(selectedStudent.getYear()));
            displayedSemester.setText("Semester: " + String.valueOf(selectedStudent.getSemester()));
            displayedMajor.setText("Major: " + selectedStudent.getMajor());
            displayedEmail.setText("Email: " + selectedStudent.getEmail());

            // Fields should be enabled for editing in the EditProfileActivity
            displayedName.setEnabled(true);
            displayedMatricNo.setEnabled(true);
            displayedYear.setEnabled(true);
            displayedSemester.setEnabled(true);
            displayedMajor.setEnabled(true);
            displayedEmail.setEnabled(true);

            icEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open the EditProfileActivity
                    Intent intent = new Intent(StudentProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("SELECTED_STUDENT", selectedStudent);
                    startActivity(intent);
                }
            });
        }
    }
}
