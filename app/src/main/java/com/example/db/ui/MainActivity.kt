package com.example.db.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.example.db.R
import com.example.db.adapter.DiffStudentCallback
import com.example.db.adapter.StudentAdapter
import com.example.db.base.BaseActivity
import com.example.db.dao.DaoMaster
import com.example.db.dao.StudentDao
import com.example.db.databinding.ActivityMainBinding
import com.example.db.db.DbUtil
import com.example.db.db.StudentHelper
import com.example.db.entity.Student
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener, OnItemChildClickListener {

    private lateinit var dbStudents: List<Student>
    private lateinit var mHelper: StudentHelper
    private var mRandom: Random? = null
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var mStudents: MutableList<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btInsert.setOnClickListener(this)
        binding.btDelete.setOnClickListener(this)
        binding.btUpdate.setOnClickListener(this)
        binding.btQuery.setOnClickListener(this)
        binding.btDeleteBatch.setOnClickListener(this)
        //当前数据库版本
        Log.w("mydb version: ", "" + DaoMaster.SCHEMA_VERSION)
        initData()
        initAdapter()
        initRefreshLayout()
    }

    private fun initData() {
        mStudents = ArrayList()
        mRandom = Random()
        mHelper = DbUtil.driverHelper
        //读取所有学生
        dbStudents = mHelper.queryAll()
        //把学生信息显示到界面
        for (s in dbStudents) {
            val item = Student()
            item.id = s.getId()
            item.name = s.getName()
            item.age = s.getAge()
            item.score = s.getScore()
            mStudents.add(item)
        }
    }

    private fun initAdapter() {
        studentAdapter = StudentAdapter(mStudents)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = studentAdapter
        // 必须设置Diff Callback
        studentAdapter.setDiffCallback(DiffStudentCallback())
        studentAdapter.addChildClickViewIds(R.id.tv_del)
        studentAdapter.setOnItemChildClickListener(this)
        studentAdapter.setList(mStudents)
    }

    private fun initRefreshLayout() {
        binding.swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
        binding.swipeLayout.setOnRefreshListener { refresh() }
    }

    /**
     * 刷新
     */
    private fun refresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        studentAdapter.loadMoreModule.isEnableLoadMore = false
        //读取所有学生
        val dbStudents = mHelper.queryAll()
        studentAdapter.setList(dbStudents)
        binding.swipeLayout.isRefreshing = false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_insert -> {
                val id = mHelper.count()
                val dbStudents = mHelper.queryAll()
                val endId = if (id == 0L) 0L else dbStudents[(id.toString() + "").toInt() - 1].id
                //界面添加
                val stu = Student()
                stu.id = endId + 1
                stu.name = "ZH_" + (endId + 1)
                stu.age = mRandom!!.nextInt(60)
                stu.score = mRandom!!.nextInt(100)
                studentAdapter.addData(stu)
                //保存到数据库s
                mHelper.save(stu)
            }
            R.id.bt_delete -> {
                //查询所有数据
                val dbStudents2 = mHelper.queryAll()
                if (dbStudents2.size > 0) {
                    //删除全部记录
                    mHelper.deleteAll()
                    dbStudents2.clear()
                    //更新界面
                    studentAdapter.setDiffNewData(dbStudents2)
                } else {
                    showToast("no student now")
                }
            }
            R.id.bt_update -> {
                //获取age大于20,小于30的数据, 修改名字
                val queryBuilder = mHelper.queryBuilder()
                val query = queryBuilder.where( /*StudentDao.Properties.Name.eq("一"),*/
                        queryBuilder.and(StudentDao.Properties.Age.gt(20),
                                StudentDao.Properties.Age.le(30)))
                        .build()
                val dbStudents3 = query.list()
                for (dbStudent in dbStudents3) {
                    dbStudent.setName("DN_" + dbStudent.id)
                }
                mHelper.update(dbStudents3)
                refresh()
            }
            R.id.bt_query -> {
                //获取age大于30的数据
                val query2 = mHelper.queryBuilder()
                        .where(StudentDao.Properties.Age.ge(30))
                        .build()
                val dbStudents4 = query2.list()
                studentAdapter.setDiffNewData(dbStudents4)
            }
            R.id.bt_deleteBatch -> {
                //批量删除分数小于50分的数据
                val studentDeleteQuery = mHelper.queryBuilder()
                        .where(StudentDao.Properties.Score.lt(50))
                        .buildDelete()
                studentDeleteQuery.executeDeleteWithoutDetachingEntities()
                refresh()
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        //查询所有数据
        dbStudents = mHelper.queryAll()
        if (dbStudents.isNotEmpty()) {
            val s = dbStudents.get(position)
            //删除一条记录
            mHelper.delete(s)
            //更新界面
            studentAdapter.removeAt(position)
        } else {
            showToast("no student now")
        }
    }

}