package com.example.todolistmvp.maintask;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bigmercu.cBox.CheckBox;
import com.example.todolistmvp.R;
import com.example.todolistmvp.util.helper.ITouchHelperAdapter;
import com.example.todolistmvp.util.helper.SimplerTouchHelperCallback;
import com.example.todolistmvp.util.room.model.Task;
import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.Showlog;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> implements ITouchHelperAdapter {

    List<Task> tasks;
    MainTaskContract.Presenter presenter;
    RecyclerView recyclerView;

    public TaskAdapter(List<Task> tasks, MainTaskContract.Presenter presenter,
                       RecyclerView recyclerView) {
        this.tasks = tasks;
        this.presenter = presenter;
        this.recyclerView = recyclerView;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);


        SimplerTouchHelperCallback simplerTouchHelperCallback =
                new SimplerTouchHelperCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simplerTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
        return tasks.size();
    }

    @Override
    public boolean onItemMove(int from, int to) {
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(tasks, i, i + 1);
            }
        } else if (from > to) {
            for (int i = from; i > to; i--) {
                Collections.swap(tasks, i, i - 1);
            }
        }
        notifyItemMoved(from, to);

        return true;
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtvTask)
        TextView txtvTask;
        @BindView(R.id.txtvTimeRemain)
        TextView txtvTimeRemain;
        @BindView(R.id.checkboxTask)
        CheckBox checkboxTask;

        Drawable defaultBackground;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            defaultBackground = itemView.getBackground();
            onClick();
        }

        private void onClick() {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                presenter.onClickItem(position);
            });

            checkboxTask.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onChange(boolean checked) {

                        int position = getAdapterPosition();
                        tasks.get(position).isComplete = checked;
                        presenter.updateData(tasks.get(position));

                        if(checked){
                            itemView.setBackgroundColor(recyclerView.getContext().getResources()
                                    .getColor(R.color.colorCompleteTask));
                        }else{
                            itemView.setBackground(defaultBackground);
                        }


                }
            });
            /*
            checkboxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    tasks.get(position).isComplete = isChecked;
                    presenter.updateData(tasks.get(position));

                    if(isChecked){
                        itemView.setBackgroundColor(recyclerView.getContext().getResources()
                        .getColor(R.color.colorCompleteTask));
                    }else{
                        itemView.setBackground(defaultBackground);
                    }

                }
            });
            */
        }

        public void bindTitle() {
            int position = getAdapterPosition();
            txtvTask.setText(tasks.get(position).title);
            String timeRemain = CommonFuntion.getTimeRemaining(recyclerView.getContext(),
                    tasks.get(position).dateAlarm);
            txtvTimeRemain.setText(timeRemain);

            if(tasks.get(position).isComplete){
                checkboxTask.setChecked(true);
            }
            Showlog.d(""+tasks.get(position).isComplete);
        }
    }
}
