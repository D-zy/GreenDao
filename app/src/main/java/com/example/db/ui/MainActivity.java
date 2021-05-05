package com.example.db.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        mRvMain = (RecyclerView) findViewById(R.id.rv_main);
        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        findViewById(R.id.bt_insert).setOnClickListener(this);
        findViewById(R.id.bt_delete).setOnClickListener(this);
        findViewById(R.id.bt_update).setOnClickListener(this);
        findViewById(R.id.bt_query).setOnClickListener(this);
        findViewById(R.id.bt_deleteBatch).setOnClickListener(this);
        //当前数据库版本
        KLog.w("db version: " + DaoMaster.SCHEMA_VERSION);

        initData();
        initAdapter();
        initRefreshLayout();
    }

    private void initData() {
        mStudents = new ArrayList<>();
        mRandom = new Random();
        mHelper = DbUtil.getDriverHelper();
        //读取所有学生
        dbStudents = mHelper.queryAll();
        //把学生信息显示到界面
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
        // 必须设置Diff Callback
        studentAdapter.setDiffCallback(new DiffStudentCallback());
        studentAdapter.setOnItemChildClickListener(this);
        studentAdapter.setList(mStudents);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    /**
     * 刷新
     */
    private void refresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        studentAdapter.getLoadMoreModule().setEnableLoadMore(false);
        //读取所有学生
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
                //界面添加
                Student stu = new Student();
                stu.id = endId + 1;
                stu.name = "ZH_" + (endId + 1);
                stu.age = mRandom.nextInt(60);
                stu.score = mRandom.nextInt(100) + "";
                studentAdapter.addData(stu);
                //保存到数据库s
                mHelper.save(stu);
                break;
            case R.id.bt_delete:
                //查询所有数据
                List<Student> dbStudents2 = mHelper.queryAll();
                if (dbStudents2.size() > 0) {
                    //删除全部记录
                    mHelper.deleteAll();
                    dbStudents2.clear();
                    //更新界面
                    studentAdapter.setDiffNewData(dbStudents2);
                } else {
                    showToast("no student now");
                }
                break;
            case R.id.bt_update:
                //获取age大于20,小于30的数据, 修改名字
                QueryBuilder<Student> queryBuilder = mHelper.queryBuilder();
                Query<Student> query = queryBuilder.where(/*StudentDao.Properties.Name.eq("一"),*/
                        queryBuilder.and(StudentDao.Properties.Age.gt(20),
                                StudentDao.Properties.Age.le(30)))
                        .build();
                List<Student> dbStudents3 = query.list();
                for (Student dbStudent : dbStudents3) {
                    dbStudent.setName("New_" + dbStudent.id);
                }
                mHelper.update(dbStudents3);
                refresh();
                break;
            case R.id.bt_query:
                //获取age大于30的数据
                Query<Student> query2 = mHelper.queryBuilder()
                        .where(StudentDao.Properties.Age.ge(30))
                        .build();
                List<Student> dbStudents4 = query2.list();
                studentAdapter.setDiffNewData(dbStudents4);
                break;
            case R.id.bt_deleteBatch:
                //批量删除分数小于50分的数据
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
     * 防多次点击的吐司
     *
     * @param msg
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
        //查询所有数据
        dbStudents = mHelper.queryAll();
        if (dbStudents.size() > 0) {
            Student s = dbStudents.get(position);
            //删除一条记录
            mHelper.delete(s);
            //更新界面
            studentAdapter.removeAt(position);
        } else {
            showToast("no student now");
        }
    }

}
