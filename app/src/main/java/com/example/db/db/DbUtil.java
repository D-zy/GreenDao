/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2015, 蒋朋, china, qd. sd
**                          All Rights Reserved
**
**                           By()
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/

package com.example.db.db;


import com.example.db.dao.StudentDao;

/**
 * 文 件 名: DbUtil
 * 说   明:  获取表 Helper 的工具类
 */
public class DbUtil {
    private static StudentHelper sStudentHelper;


    private static StudentDao getDriverDao() {
        return DbCore.getDaoSession().getStudentDao();
    }

    public static StudentHelper getDriverHelper() {
        if (sStudentHelper == null) {
            sStudentHelper = new StudentHelper(getDriverDao());
        }
        return sStudentHelper;
    }


}
