package com.example.db.db

import com.example.db.entity.Student
import org.greenrobot.greendao.AbstractDao

class StudentHelper(dao: AbstractDao<Student, Long>) : BaseDbHelper<Student, Long>(dao)