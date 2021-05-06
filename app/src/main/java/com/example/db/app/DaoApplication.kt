package com.example.db.app

import android.app.Application
import android.content.Context
import com.example.db.db.DbCore

class DaoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        _context = this
        //初始化数据库
        DbCore.init()
        DbCore.enableQueryBuilderLog() //开启调试 log
    }

    companion object {
        var _context: Application? = null
        fun getContext(): Context {
            return _context!!
        }
    }

}