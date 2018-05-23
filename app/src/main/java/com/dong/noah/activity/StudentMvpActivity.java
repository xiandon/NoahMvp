package com.dong.noah.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.dong.noah.R;
import com.dong.noah.adapter.StudentAdapter;
import com.dong.noah.entity.Student;
import com.dong.noah.mvp.presenter.StudentPresenter;
import com.dong.noah.mvp.view.StudentView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentMvpActivity extends AppCompatActivity implements StudentView {


    @BindView(R.id.lv_student)
    ListView lvStudent;

    private StudentPresenter presenter;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mvp);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        presenter = new StudentPresenter(this);
        presenter.queryStudent();
    }


    @Override
    public void showStudent(List<Student> studentList) {
        adapter = new StudentAdapter(studentList, this);
        lvStudent.setAdapter(adapter);
    }

    @Override
    public void refreshStudent() {
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_student_add, R.id.btn_student_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_student_add:
                presenter.addStudent();
                break;
            case R.id.btn_student_delete:
                presenter.deleteStudent();
                break;
        }
    }
}
