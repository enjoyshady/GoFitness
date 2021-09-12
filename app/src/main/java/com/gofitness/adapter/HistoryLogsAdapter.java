package com.gofitness.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gofitness.R;
import com.gofitness.database.entity.ExerciseLogsEntity;
import com.gofitness.database.entity.ExerciseProjectEntity;
import com.gofitness.database.entity.ExerciseProjectLogs;
import com.gofitness.view.LineItemView;

import java.util.List;

public class HistoryLogsAdapter extends RecyclerView.Adapter<HistoryLogsAdapter.ViewHolder> {

    private List<ExerciseProjectLogs> mProjectLogsList;
    private int index = 0;

    public HistoryLogsAdapter(List<ExerciseProjectLogs> mProjectLogsList) {
        this.mProjectLogsList = mProjectLogsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_delete_view, parent, false);
        itemView.setTag(viewType);
        LineItemView lineItemView = itemView.findViewById(R.id.item_view);
        lineItemView.isTop(viewType == 0);
        lineItemView.isBottom(viewType == 1);
        if (viewType == 0 || viewType == 3) {   // 是标题项目
            itemView.findViewById(R.id.delete_view).setVisibility(View.GONE);
            lineItemView.setLeftTextColor(R.color.black_translucence_eighty);
            lineItemView.setViewLayoutHeight(R.dimen.dp56);
            lineItemView.setLeftTextSize(R.dimen.sp18);
        } else {    // 是普通项目
            itemView.findViewById(R.id.delete_view).setVisibility(View.VISIBLE);
            lineItemView.setLeftTextColor(R.color.black_translucence_thirty);
            lineItemView.setViewLayoutHeight(R.dimen.dp48);
            lineItemView.setLeftTextSize(R.dimen.sp16);
        }
        return new ViewHolder(itemView, lineItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int viewType = (int) holder.itemView.getTag();
        if (viewType == 0 || viewType == 3) {
            ExerciseProjectEntity projectEntity = mProjectLogsList.get(position).getExerciseProjectEntity();
            holder.mLineItemView.setLeftText(projectEntity.getExerciseName());
            holder.mLineItemView.setItemImage(projectEntity.getImageURL());
            index = 0;
        } else {
            ExerciseLogsEntity logsEntity = mProjectLogsList.get(position).getLogsEntity();
            String leftText = String.format(holder.itemView.getContext().getString(R.string.text_format_log), ++index, logsEntity.getDescription());
            holder.mLineItemView.setLeftText(leftText);
            holder.mLineItemView.setItemImage(R.drawable.ic_transparency);
        }
    }

    @Override
    public int getItemCount() {
        return mProjectLogsList.size();
    }

    /**
     * @return -
     * 0   ： 是第一个
     * 1   ： 是此项目的最后一个
     * 2   ： 不是第一个，也不是最后一个
     * 3   ： 是此项目的第一个
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        if (position == getItemCount() - 1) return 1;
        ExerciseProjectEntity previousProjectEntity = mProjectLogsList.get(position - 1).getExerciseProjectEntity();
        ExerciseProjectEntity exerciseProjectEntity = mProjectLogsList.get(position).getExerciseProjectEntity();
        ExerciseProjectEntity nextProjectEntity = mProjectLogsList.get(position + 1).getExerciseProjectEntity();
        if (exerciseProjectEntity != previousProjectEntity) {
            // 如果这个日志的项目和上一个的项目不一样，说明这个是此项目的第一个
            return 3;
        } else if (exerciseProjectEntity == nextProjectEntity) {
            // 如果这个日志的项目和下一个的项目一样，说明这个不是此项目的最后一个日志
            return 2;
        } else { // 不一样，说明下一个换项目了
            return 1;
        }
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
                    mOnClickListener.onClickItemListener(mProjectLogsList.get(getAdapterPosition()).getLogsEntity());
                }
            });
            // 设置delete点击事件
            itemView.findViewById(R.id.delete_view).setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClickDeleteListener(mProjectLogsList.get(getAdapterPosition()).getLogsEntity());
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

