package com.dong.noah.mvp.view;

import com.dong.noah.entity.Student;

import java.util.List;

public interface StudentView {
    /**
     * 展示学生
     *
     * @param studentList
     */
    void showStudent(List<Student> studentList);

    /**
     * 刷新学生
     */
    void refreshStudent();
}
