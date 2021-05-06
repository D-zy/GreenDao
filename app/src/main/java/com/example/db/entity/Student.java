package com.example.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 文 件 名: Student
 */

@Entity
public class Student {

    @Id
    public Long id;
    public String name;
    public Integer age;
    public Integer score;
    @Generated(hash = 2068284663)
    public Student(Long id, String name, Integer age, Integer score) {
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
    public Integer getScore() {
        return this.score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }

    
}
