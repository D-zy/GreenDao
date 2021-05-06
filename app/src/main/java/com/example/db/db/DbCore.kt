package com.example.db.db

import android.annotation.SuppressLint
import android.content.Context
import com.example.db.app.DaoApplication
import com.example.db.dao.DaoMaster
import com.example.db.dao.DaoMaster.OpenHelper
import com.example.db.dao.DaoSession
import org.greenrobot.greendao.query.QueryBuilder

/**
 * 文 件 名: DbCore
 * 说   明: 核心辅助类，用于获取DaoMaster和DaoSession
 * 参   考：http://blog.inet198.cn/?sbsujjbcy/article/details/48156683
 */
object DbCore {
    private const val DEFAULT_DB_NAME = "green-dao3.db"
    private var daoMaster: DaoMaster? = null
    private var daoSession: DaoSession? = null
    private var DB_NAME: String? = null

    fun init(dbName: String? = DEFAULT_DB_NAME) {
        DB_NAME = dbName
    }

    private fun getDaoMaster(): DaoMaster {
        if (daoMaster == null) {
            //此处不可用 DaoMaster.DevOpenHelper, 那是开发辅助类，我们要自定义一个，方便升级
            val helper: OpenHelper = MyOpenHelper(DaoApplication._context, DB_NAME)
            daoMaster = DaoMaster(helper.getEncryptedReadableDb("password"))
        }
        return daoMaster!!
    }

    fun getDaoSession(): DaoSession {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster()
            }
            daoSession = daoMaster!!.newSession()
        }
        return daoSession!!
    }

    fun enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true
    }

}