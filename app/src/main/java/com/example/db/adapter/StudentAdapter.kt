package com.example.db.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.db.R
import com.example.db.entity.Student

class StudentAdapter(data: List<Student>) : BaseQuickAdapter<Student, BaseViewHolder>(R.layout.item_rv_student), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Student) {
        holder.setText(R.id.tv_id, item.id.toString())
                .setText(R.id.tv_name, item.name)
                .setTextColor(R.id.tv_name, if (item.name.startsWith("ZH")) Color.GRAY else Color.BLACK)
                .setText(R.id.tv_age, item.age.toString())
                .setText(R.id.tv_score, item.score.toString())
    }

}