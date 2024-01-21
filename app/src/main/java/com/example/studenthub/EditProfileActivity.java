package com.example.studenthub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, matricNo, year, semester, major, email;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.edit_studentName);
        matricNo = findViewById(R.id.edit_studentMatric);
        year = findViewById(R.id.edit_studentYear);
        semester = findViewById(R.id.edit_studentSemester);
        major = findViewById(R.id.edit_studentMajor);
        email = findViewById(R.id.edit_studentEmail);
        save = findViewById(R.id.button_save_editStudent);

        // Receive the Student object from the intent
        if(getIntent().hasExtra("SELECTED_STUDENT")) {
            Student selectedStudent = getIntent().getParcelableExtra("SELECTED_STUDENT");

            // Set values to the corresponding fields
            name.setText(selectedStudent.getName());
            matricNo.setText(selectedStudent.getMatricNo());
            year.setText(String.valueOf(selectedStudent.getYear()));
            semester.setText(String.valueOf(selectedStudent.getSemester()));
            major.setText(selectedStudent.getMajor());
            email.setText(selectedStudent.getEmail());
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student updatedStudent = new Student(
                        name.getText().toString(),
                        matricNo.getText().toString(),
                        Integer.parseInt(year.getText().toString()),
                        Integer.parseInt(semester.getText().toString()),
                        major.getText().toString(),
                        email.getText().toString()
                );

                // Update the StudentProfileActivity with the new data
                updateStudentProfile(new Student(
                        name.getText().toString(),
                        matricNo.getText().toString(),
                        Integer.parseInt(year.getText().toString()),
                        Integer.parseInt(semester.getText().toString()),
                        major.getText().toString(),
                        email.getText().toString()
                ));
            }
        });
    }

    private void instantSave(Student student) {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        resultIntent.putExtra("UPDATED_STUDENT", student);
        finish();
    }

    private void updateStudentProfile(Student student) {
        // Send an intent with the updated student data to the StudentProfileActivity
        Intent updateIntent = new Intent(EditProfileActivity.this, StudentProfileActivity.class);
        updateIntent.putExtra("SELECTED_STUDENT", student);
        startActivity(updateIntent);
    }
}
