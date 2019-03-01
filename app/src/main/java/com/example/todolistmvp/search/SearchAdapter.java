package com.example.todolistmvp.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.todolistmvp.R;
import com.example.todolistmvp.room.model.Task;
import com.example.todolistmvp.util.CommonFuntion;
import com.example.todolistmvp.util.Showlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> implements Filterable {

    List<Task> mTasks;
    List<Task> mTasksSearch = new ArrayList<>();
    SearchContract.Presenter mPresenter;
    RecyclerView mRecyclerView;

    HashMap<Integer,Integer> mappingId = new HashMap<>();

    public SearchAdapter(List<Task> tasks, SearchContract.Presenter presenter,
                       RecyclerView recyclerView) {
        this.mTasks = tasks;
        this.mPresenter = presenter;
        this.mRecyclerView = recyclerView;
        mTasksSearch.addAll(tasks);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

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
        return mTasksSearch.size();
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
                mPresenter.onClickItem(mappingId.get(mTasksSearch.get(position).id));
            });
        }

        public void bindTitle() {
            int position = getAdapterPosition();
            txtvTask.setText(mTasksSearch.get(position).title);
            String timeRemain = CommonFuntion.getTimeRemaining(mTasksSearch.get(position).dateAlarm);
            txtvTimeRemain.setText(timeRemain);
        }
    }


    @Override
    public Filter getFilter() {
        return new ItemFilter();
    }

    class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            if (constraint.toString().trim().equals("")) {
                return null;
            }
            FilterResults results = new FilterResults();
            ArrayList<Task> nlist = new ArrayList<>();

            mTasksSearch.clear();

            for (int i = 0; i < mTasks.size(); i++) {

                if (CommonFuntion.isMatch(mTasks.get(i).title, constraint.toString())) {
                    nlist.add(mTasks.get(i));
                    mappingId.put(mTasks.get(i).id,i);

                }
            }
            results.values = nlist;
            results.count = nlist.size();
            Showlog.d("result return: " + results);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results == null) {
                mTasksSearch.clear();
            } else {
                if(results.values!=null) {
                    mTasksSearch = (ArrayList<Task>) results.values;
                }
            }
            notifyDataSetChanged();
        }
    }

}
