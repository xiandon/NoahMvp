package com.dong.noah.mvp.model.impl;

import com.dong.noah.entity.Student;

import java.util.List;

public interface IStudent {
    /**
     * 查询所有学生
     *
     * @param listener 回调
     */
    void query(onQueryListener listener);

    /**
     * 添加学生
     *
     * @param listener 回调
     */
    void addStudent(onAddStudentListener listener);

    /**
     * 删除学生
     *
     * @param listener 回调
     */
    void deleteStudent(onDeleteStudentListener listener);

    /**
     * 查询学生回调
     */
    interface onQueryListener {
        void onComplete(List<Student> studentList);
    }

    /**
     * 添加学生回调
     */
    interface onAddStudentListener {
        void onComplete();
    }

    /**
     * 删除学生回调
     */
    interface onDeleteStudentListener {
        void onComplete();
    }

}
