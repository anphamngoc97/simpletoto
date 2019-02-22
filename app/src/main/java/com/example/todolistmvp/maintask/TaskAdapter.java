package com.example.todolistmvp.maintask;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todolistmvp.R;
import com.example.todolistmvp.room.model.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends  RecyclerView.Adapter<TaskAdapter.Holder> {

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

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task,viewGroup,false);
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

    class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtvTask)
        TextView txtvTask;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        public void bindTitle(){
            int position = getAdapterPosition();
            txtvTask.setText(tasks.get(position).title);
        }
    }
}
