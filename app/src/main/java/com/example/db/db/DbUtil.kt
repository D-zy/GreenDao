package com.example.db.db

import com.example.db.dao.StudentDao

/**
 * 文 件 名: DbUtil
 * 说   明:  获取表 Helper 的工具类
 */
object DbUtil {

    private var sStudentHelper: StudentHelper? = null

    private val driverDao: StudentDao
        get() = DbCore.getDaoSession().studentDao

    val driverHelper: StudentHelper
        get() {
            if (sStudentHelper == null) {
                sStudentHelper = StudentHelper(driverDao)
            }
            return sStudentHelper!!
        }
}