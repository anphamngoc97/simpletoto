package com.example.todolistmvp.maintask;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.bigmercu.cBox.CheckBox;
import com.example.todolistmvp.R;
import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.helper.ITouchHelperAdapter;
import com.example.todolistmvp.util.room.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> implements ITouchHelperAdapter {

    List<Task> taskList;
    OnRecyclerViewItemClick onItemClick;
    Context context;

    public interface OnRecyclerViewItemClick {
        void onItemClick(int position, Task task);

        void onCheckBoxChange(int position, Task task, boolean isChecked);
    }

    public TaskAdapter(Context context) {
        this.taskList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.bindTitle();
        final ViewTreeObserver observer = holder.checkboxTask.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.checkboxTask.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    holder.checkboxTask.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int containerWidth = holder.checkboxTask.getWidth();
                int containerHeight = holder.checkboxTask.getHeight();
                holder.updateSizeItem(containerWidth,containerHeight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public boolean onItemMove(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(taskList, i, i + 1);
            }
        } else if (from > to) {
            for (int i = from; i > to; i--) {
                Collections.swap(taskList, i, i - 1);
            }
        }
        notifyItemMoved(from, to);

        return true;
    }

    public void setOnItemClick(OnRecyclerViewItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public void update(int position, Task task) {
        taskList.set(position, task);
        notifyItemChanged(position);
        //notifyDataSetChanged();
    }

    public void remove(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void insert(Task task) {
        taskList.add(task);
        notifyItemInserted(taskList.size());
    }

    public void insert(List<Task> tasks) {
        taskList.addAll(tasks);
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtvTask)
        TextView txtvTask;
        @BindView(R.id.txtvTimeRemain)
        TextView txtvTimeRemain;
        @BindView(R.id.checkboxTask)
        CheckBox checkboxTask;
        @BindView(R.id.imgPriority)
        Button imgPriority;
        @BindView(R.id.txtvPriority)
        TextView txtvPriority;
        @BindView(R.id.txtvCategory)
        TextView txtvCategory;
        @BindView(R.id.groupPriority)
        ViewGroup groupPriority;

        Color colorPriority;

        final Drawable defaultBackground;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            defaultBackground = itemView.getBackground();
            onClick();
        }


        private void onClick() {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                //presenter.onClickItem(position,taskList.get(position));
                if (onItemClick != null) {
                    onItemClick.onItemClick(position, taskList.get(position));
                }
            });

            checkboxTask.setOnClickListener(v -> {
                int position = getLayoutPosition();

                if (onItemClick != null) {
                    onItemClick.onCheckBoxChange(position, taskList.get(position),
                            !checkboxTask.isChecked());
                }
            });

        }

        private void bindTitle() {
            int position = getLayoutPosition();

            txtvTask.setText(taskList.get(position).title);
            String timeRemain = CommonFuntion.getTimeRemaining(context,
                    taskList.get(position).dateAlarm);
            txtvTimeRemain.setText(timeRemain);

            if (taskList.get(position).isComplete != checkboxTask.isChecked()) {
                checkboxTask.setChecked(taskList.get(position).isComplete);

                if (checkboxTask.isChecked()) {
                    itemView.setBackgroundColor(context.getResources()
                            .getColor(R.color.colorCompleteTask));
                } else {
                    itemView.setBackground(defaultBackground);
                }
            }


            setUpPriority(position);
            setUpCategory(position);

        }
        public void updateSizeItem(int width,int height){
            checkboxTask.setPadding(0,(int)(height*0.3),0,0);
        }

        private void setUpPriority(int position) {

            String priorityString = taskList.get(position).tag;
            Constant.ChildConstantDetailTaskPriority priority =
                    CommonFuntion.getPriority(priorityString);


            if (priority != null) {
                groupPriority.setVisibility(View.VISIBLE);
                switch (priority) {
                    case HIGH: {
                        //imgPriority.setBackgroundColor(context.getResources()
                        //      .getColor(R.color.colorHighPriority));
                        imgPriority.setBackgroundResource(R.drawable.background_priorty_hight);
                        txtvPriority.setText(priority.getValueAsLanguage(context));
                        break;
                    }
                    case MEDIUM: {
//                        imgPriority.setBackgroundColor(context.getResources()
//                                .getColor(R.color.colorMediumPriority));
                        imgPriority.setBackgroundResource(R.drawable.background_priority_medium);
                        txtvPriority.setText(priority.getValueAsLanguage(context));
                        break;
                    }
                    case LOW: {
//                        imgPriority.setBackgroundColor(context.getResources()
//                                .getColor(R.color.colorLowPriority));
                        imgPriority.setBackgroundResource(R.drawable.background_priority_low);
                        txtvPriority.setText(priority.getValueAsLanguage(context));
                        break;
                    }
                    default: {
//                        imgPriority.setBackgroundColor(context.getResources()
//                                .getColor(R.color.colorNonePriority));
                        imgPriority.setBackgroundResource(R.drawable.background_priority_none);
                        txtvPriority.setText(Constant
                                .ChildConstantDetailTaskPriority
                                .NONE
                                .getValueAsLanguage(context));
                    }
                }
            } else {
                groupPriority.setVisibility(View.GONE);
            }
        }

        private void setUpCategory(int position) {
            String categoryString = taskList.get(position).typeList;
            Constant.ChildConstantDetailTaskCategory category =
                    CommonFuntion.getCategory(categoryString);
            if (category != null) {
                switch (category) {
                    case BUSINESS: {
                        txtvCategory.setText(category.getValueAsLanguage(context));
                        break;
                    }
                    case PERSONAL: {
                        txtvCategory.setText(category.getValueAsLanguage(context));
                        break;
                    }
                    default: {
                        txtvCategory.setText(Constant
                                .ChildConstantDetailTaskCategory
                                .NONE
                                .getValueAsLanguage(context));
                    }
                }
            } else {
                txtvCategory.setText(Constant
                        .ChildConstantDetailTaskCategory
                        .NONE
                        .getValueAsLanguage(context));
            }
        }

    }
}
