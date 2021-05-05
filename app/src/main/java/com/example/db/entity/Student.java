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

package com.example.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 文 件 名: Student
 * 创 建 人: 蒋朋
 * 创建日期: 16-8-26 17:25
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */

@Entity
public class Student {

    @Id
    public Long id;
    public String name;
    public Integer age;
    public String score;
    @Generated(hash = 1760753087)
    public Student(Long id, String name, Integer age, String score) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.score = score;
    }
    @Generated(hash = 1556870573)
    public Student() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return this.age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public String getScore() {
        return this.score;
    }
    public void setScore(String score) {
        this.score = score;
    }
    
}
