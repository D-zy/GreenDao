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

import com.example.db.entity.Student;

import org.greenrobot.greendao.AbstractDao;

/**
 * 文 件 名: StudentHelper
 * 说   明: 具体表的实现类
 */
public class StudentHelper extends BaseDbHelper<Student, Long> {


    public StudentHelper(AbstractDao dao) {
        super(dao);
    }
}
