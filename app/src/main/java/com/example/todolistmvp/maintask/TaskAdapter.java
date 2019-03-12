package com.example.todolistmvp.maintask;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigmercu.cBox.CheckBox;
import com.example.todolistmvp.R;
import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.helper.ITouchHelperAdapter;
import com.example.todolistmvp.util.room.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> implements ITouchHelperAdapter{

    List<Task> taskList;
    OnRecyclerViewItemClick onItemClick;
    Context context;

    public interface OnRecyclerViewItemClick {
        void onItemClick(int position, Task task);
        void onCheckBoxChange(int position,Task task,boolean isChecked);
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
    public void update(int position, Task task){
        taskList.set(position,task);
        notifyItemChanged(position);
        //notifyDataSetChanged();
    }
    public void remove(int position){
        taskList.remove(position);
        notifyItemRemoved(position);
    }
    public void insert(Task task){
        taskList.add(task);
        notifyItemInserted(taskList.size());
    }
    public void insert(List<Task> tasks){
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

                //todo complete presenter
                if (onItemClick != null) {
                    onItemClick.onItemClick(position,taskList.get(position));
                }
            });

            checkboxTask.setOnClickListener(v->{
                int position = getAdapterPosition();

                if (onItemClick != null) {
                    onItemClick.onCheckBoxChange(position,taskList.get(position),
                            !checkboxTask.isChecked());
                }
            });
            checkboxTask.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onChange(boolean checked) {
                    int position = getAdapterPosition();

                    /*
                        taskList.get(position).isComplete = checked;
                        presenter.updateData(taskList.get(position));

                        if(checked){
                            itemView.setBackgroundColor(recyclerView.getContext().getResources()
                                    .getColor(R.color.colorCompleteTask));
                        }else{
                            itemView.setBackground(defaultBackground);
                         }
                                                    */
                    //todo complete presenter
                    if (onItemClick != null) {
                        //onItemClick.onCheckBoxChange(position,taskList.get(position),checked);
                    }

                }
            });
        }

        public void bindTitle() {
            int position = getAdapterPosition();
            txtvTask.setText(taskList.get(position).title);
            String timeRemain = CommonFuntion.getTimeRemaining(context,
                    taskList.get(position).dateAlarm);
            txtvTimeRemain.setText(timeRemain);

            if (taskList.get(position).isComplete != checkboxTask.isChecked()) {
                checkboxTask.setChecked(taskList.get(position).isComplete);

                if(checkboxTask.isChecked()){
                    itemView.setBackgroundColor(context.getResources()
                            .getColor(R.color.colorCompleteTask));
                }else{
                    itemView.setBackground(defaultBackground);
                }

            }
        }
    }
}
