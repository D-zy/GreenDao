package com.example.db.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.db.R;
import com.example.db.adapter.DiffStudentCallback;
import com.example.db.adapter.StudentAdapter;
import com.example.db.dao.DaoMaster;
import com.example.db.dao.StudentDao;
import com.example.db.db.DbUtil;
import com.example.db.db.StudentHelper;
import com.example.db.entity.Student;
import com.socks.library.KLog;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemChildClickListener {

    private RecyclerView mRvMain;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Student> dbStudents;
    private StudentHelper mHelper;
    private Random mRandom;
    private StudentAdapter studentAdapter;
    private List<Student> mStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvMain = findViewById(R.id.rv_main);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        findViewById(R.id.bt_insert).setOnClickListener(this);
        findViewById(R.id.bt_delete).setOnClickListener(this);
        findViewById(R.id.bt_update).setOnClickListener(this);
        findViewById(R.id.bt_query).setOnClickListener(this);
        findViewById(R.id.bt_deleteBatch).setOnClickListener(this);
        //?????????????????????
        KLog.w("db version: " + DaoMaster.SCHEMA_VERSION);

        initData();
        initAdapter();
        initRefreshLayout();
    }

    private void initData() {
        mStudents = new ArrayList<>();
        mRandom = new Random();
        mHelper = DbUtil.getDriverHelper();
        //??????????????????
        dbStudents = mHelper.queryAll();
        //??????????????????????????????
        for (Student s : dbStudents) {
            Student item = new Student();
            item.id = s.getId();
            item.name = s.getName();
            item.age = s.getAge();
            item.score = s.getScore();
            mStudents.add(item);
            KLog.w("db: " + item.id + ", " + item.age + ", " + item.name);
        }
    }

    private void initAdapter() {
        studentAdapter = new StudentAdapter(mStudents);
        mRvMain.setLayoutManager(new LinearLayoutManager(this));
        mRvMain.setAdapter(studentAdapter);
        // ????????????Diff Callback
        studentAdapter.setDiffCallback(new DiffStudentCallback());
        studentAdapter.setOnItemChildClickListener(this);
        studentAdapter.setList(mStudents);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setOnRefreshListener(this::refresh);
    }

    /**
     * ??????
     */
    private void refresh() {
        // ??????????????????????????????????????????????????????????????????
        studentAdapter.getLoadMoreModule().setEnableLoadMore(false);
        //??????????????????
        List<Student> dbStudents = mHelper.queryAll();
        studentAdapter.setList(dbStudents);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_insert:
                long id = mHelper.count();
                List<Student> dbStudents = mHelper.queryAll();
                Long endId;
                if (id == 0) {
                    endId = 0L;
                } else {
                    endId = dbStudents.get(Integer.parseInt(id + "") - 1).id;
                }
                //????????????
                Student stu = new Student();
                stu.id = endId + 1;
                stu.name = "ZH_" + (endId + 1);
                stu.age = mRandom.nextInt(60);
                stu.score = mRandom.nextInt(100);
                studentAdapter.addData(stu);
                //??????????????????s
                mHelper.save(stu);
                break;
            case R.id.bt_delete:
                //??????????????????
                List<Student> dbStudents2 = mHelper.queryAll();
                if (dbStudents2.size() > 0) {
                    //??????????????????
                    mHelper.deleteAll();
                    dbStudents2.clear();
                    //????????????
                    studentAdapter.setDiffNewData(dbStudents2);
                } else {
                    showToast("no student now");
                }
                break;
            case R.id.bt_update:
                //??????age??????20,??????30?????????, ????????????
                QueryBuilder<Student> queryBuilder = mHelper.queryBuilder();
                Query<Student> query = queryBuilder.where(/*StudentDao.Properties.Name.eq("???"),*/
                        queryBuilder.and(StudentDao.Properties.Age.gt(20),
                                StudentDao.Properties.Age.le(30)))
                        .build();
                List<Student> dbStudents3 = query.list();
                for (Student dbStudent : dbStudents3) {
                    dbStudent.setName("DN_" + dbStudent.id);
                }
                mHelper.update(dbStudents3);
                refresh();
                break;
            case R.id.bt_query:
                //??????age??????30?????????
                Query<Student> query2 = mHelper.queryBuilder()
                        .where(StudentDao.Properties.Age.ge(30))
                        .build();
                List<Student> dbStudents4 = query2.list();
                studentAdapter.setDiffNewData(dbStudents4);
                break;
            case R.id.bt_deleteBatch:
                //????????????????????????50????????????
                DeleteQuery<Student> studentDeleteQuery = mHelper.queryBuilder()
                        .where(StudentDao.Properties.Score.lt(50))
                        .buildDelete();
                studentDeleteQuery.executeDeleteWithoutDetachingEntities();
                refresh();
                break;
        }
    }

    private Toast mToast;

    /**
     * ????????????????????????
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.show();
        }
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        //??????????????????
        dbStudents = mHelper.queryAll();
        if (dbStudents.size() > 0) {
            Student s = dbStudents.get(position);
            //??????????????????
            mHelper.delete(s);
            //????????????
            studentAdapter.removeAt(position);
        } else {
            showToast("no student now");
        }
    }

}
