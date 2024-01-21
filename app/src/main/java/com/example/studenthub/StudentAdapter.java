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
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private OnItemClickListener onItemClickListener;

    public StudentAdapter() {
        this.studentList = new ArrayList<>();
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
        final Student student = studentList.get(position);
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
        return studentList.size();
    }

    public void addStudent(Student student) {
        studentList.add(student);
        notifyItemInserted(studentList.size() - 1);
    }

    public void removeStudent(Student student) {
        int position = studentList.indexOf(student);
        if (position != -1) {
            studentList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateStudent(Student updatedStudent) {
        int position = studentList.indexOf(updatedStudent);
        if (position != -1) {
            studentList.set(position, updatedStudent);
            notifyItemChanged(position);
        }
    }

    public void clearStudents() {
        studentList.clear();
        notifyDataSetChanged();
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
            studentName = itemView.findViewById(R.id.student_name);
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
                // Handle menu item click
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
}
