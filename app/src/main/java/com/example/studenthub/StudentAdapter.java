package com.example.studenthub;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private CustomArrayList<Student> studentList;
    private CustomArrayList<Student> filteredStudentList;
    private OnItemClickListener onItemClickListener;
    private String searchText = "";

    public StudentAdapter() {
        this.studentList = new CustomArrayList<>();
        this.filteredStudentList = new CustomArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_box, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        final Student student = filteredStudentList.get(position);
        holder.bind(student);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(student);
                }
            }
        });

        holder.icMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredStudentList.size();
    }


    public void setStudentList(CustomArrayList<Student> students) {
        studentList.clear();
        studentList.addAll(students);
        filter(searchText); // Reapply the filter
    }

    public interface OnItemClickListener {
        void onItemClick(Student student);
        void onDeleteClick(Student student);
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView studentName;
        private ImageView icMore;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.delete_student_name);
            icMore = itemView.findViewById(R.id.ic_more);
        }

        public void bind(Student student) {
            studentName.setText(student.getName());
        }
    }

    private void showPopupMenu(View view, final Student student) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.student_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_delete) {
                    // Delete action
                    if (onItemClickListener != null) {
                        onItemClickListener.onDeleteClick(student);
                    }
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();
    }

    public void filter(String text) {
        searchText = text; // Update searchText
        filteredStudentList.clear();

        if (text.isEmpty()) {
            // If the search box is empty, show all students
            filteredStudentList.addAll(studentList);
        } else {
            // Filter students based on the entered text
            text = text.toLowerCase();
            for (int i = 0; i < studentList.size(); i++) {
                Student student = studentList.get(i);
                if (student.getName().toLowerCase().contains(text)) {
                    filteredStudentList.add(student);
                }
            }
        }

        notifyDataSetChanged();
    }
}

