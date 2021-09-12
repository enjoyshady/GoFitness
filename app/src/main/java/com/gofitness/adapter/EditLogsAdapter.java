package com.gofitness.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gofitness.R;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.view.LineItemView;

import java.util.List;

public class EditLogsAdapter extends RecyclerView.Adapter<EditLogsAdapter.ViewHolder> {

    private List<ExerciseLogsEntity> mExerciseLogsEntities;

    public EditLogsAdapter(List<ExerciseLogsEntity> mExerciseLogsEntities) {
        this.mExerciseLogsEntities = mExerciseLogsEntities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_delete_view, parent, false);
        LineItemView lineItemView = itemView.findViewById(R.id.item_view);
        lineItemView.isTop(viewType == 0 || viewType == -1);
        lineItemView.isBottom(viewType == 1 || viewType == -1);
        return new ViewHolder(itemView, lineItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseLogsEntity logsEntity = mExerciseLogsEntities.get(position);
        String leftText = String.format(holder.itemView.getContext().getString(R.string.text_format_log), position + 1, logsEntity.getDescription());
        holder.mLineItemView.setLeftText(leftText);
//        holder.mLineItemView.setItemImage(R.drawable.ic_transparency);
    }

    @Override
    public int getItemCount() {
        return mExerciseLogsEntities.size();
    }

    /**
     * @return -
     * 0   ： 是第一个
     * 1   ： 是最后一个
     * 2   ： 不是第一个，也不是最后一个
     */
    @Override
    public int getItemViewType(int position) {
//        if (position == 0 && getItemCount() == 1)
//            return -1;
        if (position == 0)
            return 0;
//        if (position == getItemCount() - 1)
//            return 1;
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LineItemView mLineItemView;

        @SuppressLint("NonConstantResourceId")
        public ViewHolder(@NonNull View itemView, LineItemView lineItemView) {
            super(itemView);
            mLineItemView = lineItemView;
            mLineItemView.setLeftTextColor(R.color.black_translucence_eighty);
            // 设置item点击事件
            mLineItemView.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClickItemListener(mExerciseLogsEntities.get(getAdapterPosition()));
                }
            });
            // 设置delete点击事件
            itemView.findViewById(R.id.delete_view).setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClickDeleteListener(mExerciseLogsEntities.get(getAdapterPosition()));
                }
            });
        }
    }


    /**
     * 点击接口  供外部调用
     */
    public interface OnClickListener {
        void onClickItemListener(ExerciseLogsEntity entity);

        void onClickDeleteListener(ExerciseLogsEntity entity);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

}

