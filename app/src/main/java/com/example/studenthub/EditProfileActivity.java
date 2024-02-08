package com.example.studenthub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, matricNo, year, semester, major, email;
    Button save;
    Student selectedStudent;

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

        if (getIntent().hasExtra("SELECTED_STUDENT")) {
            selectedStudent = getIntent().getParcelableExtra("SELECTED_STUDENT");

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
                updateStudentData();
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void updateStudentData() {
        String studentName = name.getText().toString();
        String originalMatricNo = selectedStudent.getMatricNo(); // Original matric no before update
        String updatedMatricNo = matricNo.getText().toString();
        int studentYear = Integer.parseInt(year.getText().toString());
        int studentSemester = Integer.parseInt(semester.getText().toString());
        String studentMajor = major.getText().toString();
        String studentEmail = email.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("StudentPreferences", Context.MODE_PRIVATE);
        int numberOfStudents = sharedPreferences.getInt("numberOfStudents", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Check if matricNo has been changed
        if (!originalMatricNo.equals(updatedMatricNo)) {
            // Handle matricNo change
            for (int i = 1; i <= numberOfStudents; i++) {
                String storedMatricNo = sharedPreferences.getString("matricNo" + i, "");

                if (storedMatricNo.equals(originalMatricNo)) {
                    // Remove the old student data
                    editor.remove("name" + i);
                    editor.remove("matricNo" + i);
                    editor.remove("year" + i);
                    editor.remove("semester" + i);
                    editor.remove("major" + i);
                    editor.remove("email" + i);

                    // Shift the data for students after the deleted one
                    for (int j = i; j < numberOfStudents; j++) {
                        editor.putString("name" + j, sharedPreferences.getString("name" + (j + 1), ""));
                        editor.putString("matricNo" + j, sharedPreferences.getString("matricNo" + (j + 1), ""));
                        editor.putInt("year" + j, sharedPreferences.getInt("year" + (j + 1), -1));
                        editor.putInt("semester" + j, sharedPreferences.getInt("semester" + (j + 1), -1));
                        editor.putString("major" + j, sharedPreferences.getString("major" + (j + 1), ""));
                        editor.putString("email" + j, sharedPreferences.getString("email" + (j + 1), ""));
                    }

                    // Decrement the number of students
                    editor.putInt("numberOfStudents", numberOfStudents - 1);
                    break;
                }
            }

            // Add the student with updated matricNo
            editor.putString("name" + (numberOfStudents), studentName);
            editor.putString("matricNo" + (numberOfStudents), updatedMatricNo);
            editor.putInt("year" + (numberOfStudents), studentYear);
            editor.putInt("semester" + (numberOfStudents), studentSemester);
            editor.putString("major" + (numberOfStudents), studentMajor);
            editor.putString("email" + (numberOfStudents), studentEmail);
        } else {
            // Update student data without changing matricNo
            for (int i = 1; i <= numberOfStudents; i++) {
                String storedMatricNo = sharedPreferences.getString("matricNo" + i, "");

                if (storedMatricNo.equals(originalMatricNo)) {
                    editor.putString("name" + i, studentName);
                    editor.putString("matricNo" + i, updatedMatricNo);
                    editor.putInt("year" + i, studentYear);
                    editor.putInt("semester" + i, studentSemester);
                    editor.putString("major" + i, studentMajor);
                    editor.putString("email" + i, studentEmail);
                    break;
                }
            }
        }

        editor.apply();

        // Updating the selected student's data
        selectedStudent.setName(studentName);
        selectedStudent.setMatricNo(updatedMatricNo);
        selectedStudent.setYear(studentYear);
        selectedStudent.setSemester(studentSemester);
        selectedStudent.setMajor(studentMajor);
        selectedStudent.setEmail(studentEmail);

        // Setting the updated data in the UI
        name.setText(studentName);
        matricNo.setText(updatedMatricNo);
        year.setText(String.valueOf(studentYear));
        semester.setText(String.valueOf(studentSemester));
        major.setText(studentMajor);
        email.setText(studentEmail);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_STUDENT", selectedStudent);
        setResult(RESULT_OK, resultIntent);

    }
}
