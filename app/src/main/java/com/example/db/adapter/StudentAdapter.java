package com.example.db.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.db.R;
import com.example.db.entity.Student;

import java.util.List;

public class StudentAdapter extends BaseQuickAdapter<Student, BaseViewHolder> implements LoadMoreModule {

    public StudentAdapter(List<Student> data) {
        super(R.layout.item_rv_student, data);
        addChildClickViewIds(R.id.tv_del);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Student item) {
        helper.setText(R.id.tv_id, String.valueOf(item.id))
                .setText(R.id.tv_name, item.name)
                .setText(R.id.tv_age, String.valueOf(item.age))
                .setText(R.id.tv_score, String.valueOf(item.score));
        if (item.name.startsWith("ZH")) {
            helper.setTextColor(R.id.tv_name, Color.GRAY);
        } else {
            helper.setTextColor(R.id.tv_name, Color.BLACK);
        }

    }

}

