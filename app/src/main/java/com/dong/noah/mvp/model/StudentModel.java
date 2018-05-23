package com.dong.noah.mvp.model;

import com.dong.noah.R;
import com.dong.noah.entity.Student;
import com.dong.noah.mvp.model.impl.IStudent;
import com.ljy.devring.DevRing;

import java.util.ArrayList;
import java.util.List;

public class StudentModel implements IStudent {

    private static List<Student> students = new ArrayList<>();

    static {
        students.add(new Student("佩奇", R.mipmap.ic_launcher));
        students.add(new Student("乔治", R.mipmap.ic_launcher));
        students.add(new Student("小猪", R.mipmap.ic_launcher));
    }

    @Override
    public void query(onQueryListener listener) {
        if (listener != null) {
            listener.onComplete(students);
        }
    }

    @Override
    public void addStudent(onAddStudentListener listener) {
        students.add(new Student("猪妈妈", R.drawable.ic_launcher_foreground));
        if (listener != null) {
            listener.onComplete();
        }
    }

    @Override
    public void deleteStudent(onDeleteStudentListener listener) {
        if (students.size() > 0) {
            students.remove(students.size() - 1);
        }

        if (listener != null) {
            listener.onComplete();
        }
    }
}
