package com.example.db.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.example.db.entity.Student;

/**
 * Create DiffCallback
 */
public class DiffStudentCallback extends DiffUtil.ItemCallback<Student> {

    /**
     * Determine if it is the same item
     * <p>
     * 判断是否是同一个item
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    /**
     * When it is the same item, judge whether the content has changed.
     * <p>
     * 当是同一个item时，再判断内容是否发生改变
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
        return oldItem.getId().equals(newItem.getId())
                && oldItem.getAge().equals(newItem.getAge())
                && oldItem.getName().equals(newItem.getName())
                && oldItem.getScore().equals(newItem.getScore());
    }

    /**
     * Optional implementation
     * Implement this method if you need to precisely modify the content of a view.
     * If this method is not implemented, or if null is returned, the entire item will be refreshed.
     *
     * 可选实现
     * 如果需要精确修改某一个view中的内容，请实现此方法。
     * 如果不实现此方法，或者返回null，将会直接刷新整个item。
     *
     * @param oldItem Old data
     * @param newItem New data
     * @return Payload info. if return null, the entire item will be refreshed.
     */
    @Override
    public Object getChangePayload(@NonNull Student oldItem, @NonNull Student newItem) {
        return null;
    }
}