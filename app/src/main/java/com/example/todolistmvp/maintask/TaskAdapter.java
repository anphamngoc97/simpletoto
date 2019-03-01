package com.example.todolistmvp.maintask;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todolistmvp.R;
import com.example.todolistmvp.adapter.helper.ITouchHelperAdapter;
import com.example.todolistmvp.adapter.helper.SimplerTouchHelperCallback;
import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.util.CommonFuntion;

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
        notifyItemMoved(from,to);

        return true;
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtvTask)
        TextView txtvTask;
        @BindView(R.id.txtvTimeRemain)
        TextView txtvTimeRemain;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            onClick();
        }

        private void onClick() {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                presenter.onClickItem(position);
            });
        }

        public void bindTitle() {
            int position = getAdapterPosition();
            txtvTask.setText(tasks.get(position).title);
            String timeRemain = CommonFuntion.getTimeRemaining(tasks.get(position).dateAlarm);
            txtvTimeRemain.setText(timeRemain);
        }
    }
}
