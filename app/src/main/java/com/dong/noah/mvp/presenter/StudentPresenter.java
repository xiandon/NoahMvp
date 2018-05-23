package com.dong.noah.mvp.presenter;

import com.dong.noah.entity.Student;
import com.dong.noah.mvp.model.StudentModel;
import com.dong.noah.mvp.model.impl.IStudent;
import com.dong.noah.mvp.presenter.impl.IStudentPresenter;
import com.dong.noah.mvp.view.StudentView;

import java.util.List;

public class StudentPresenter implements IStudentPresenter {
    private StudentModel studentModel;
    private StudentView studentView;

    public StudentPresenter(StudentView studentView) {
        studentModel = new StudentModel();
        this.studentView = studentView;
    }

    @Override
    public void queryStudent() {
        studentModel.query(new IStudent.onQueryListener() {
            @Override
            public void onComplete(List<Student> studentList) {
                studentView.showStudent(studentList);
            }
        });
    }

    @Override
    public void addStudent() {
        studentModel.addStudent(new IStudent.onAddStudentListener() {
            @Override
            public void onComplete() {
                studentView.refreshStudent();
            }
        });
    }

    @Override
    public void deleteStudent() {
        studentModel.deleteStudent(new IStudent.onDeleteStudentListener() {
            @Override
            public void onComplete() {
                studentView.refreshStudent();
            }
        });
    }
}
