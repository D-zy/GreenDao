package com.example.db.db

import android.content.Context
import android.util.Log
import com.example.db.dao.DaoMaster.OpenHelper
import com.example.db.dao.StudentDao
import org.greenrobot.greendao.database.Database

/**
 * 文 件 名: MyOpenHelper
 */
class MyOpenHelper(context: Context?, name: String?) : OpenHelper(context, name) {

    override fun onUpgrade(db: Database, oldVersion: Int, newVersion: Int) {
        Log.w("mydb:", "db version update from $oldVersion to $newVersion")
        when (oldVersion) {
            1 -> {
                //不能先删除表，否则数据都木了
//                StudentDao.dropTable(db, true);
                StudentDao.createTable(db, true)

                // 加入新字段 score
                db.execSQL("ALTER TABLE 'STUDENT' ADD 'SCORE' TEXT;")
            }
        }
    }

}