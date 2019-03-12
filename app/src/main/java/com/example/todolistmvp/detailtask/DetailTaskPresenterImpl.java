package com.example.todolistmvp.detailtask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.todolistmvp.R;
import com.example.todolistmvp.util.Constant;
import com.example.todolistmvp.util.Showlog;

public class DetailTaskPresenterImpl implements DetailTaskContract.Presenter {
    DetailTaskContract.View view;

    String categorySelected = "";
    String prioritySelected = "";
    Context context;
    //Class prevIntent;

    public DetailTaskPresenterImpl(Context context, Intent data, DetailTaskContract.View view) {
        setView(view);
        this.context = context;
        getData(data);
    }

    @Override
    public void setView(DetailTaskContract.View view) {
        this.view = view;
    }

    private void getData(Intent data) {

//        prevIntent = (Class) data
//                .getSerializableExtra(Constant.ChildConstantString.KEY_EXTRA_PREVIOUS_CLASS.getValue());
        Bundle bundle = data.getBundleExtra(
                Constant.ChildConstantString.KEY_EXTRA_BUNDLE_DETAIL_TASK.getValue());

        if (bundle != null) {
            prioritySelected = bundle.getString(
                    Constant.ChildConstantString.KEY_EXTRA_TASK_PRIORITY.getValue());
            categorySelected = bundle.getString(
                    Constant.ChildConstantString.KEY_EXTRA_TASK_CATEGORY.getValue());
            String detail = bundle.getString(
                    Constant.ChildConstantString.KEY_EXTRA_TASK_DETAIL.getValue());

            Showlog.d("detail receive: " + prioritySelected+"_"+categorySelected);
            view.updateDetail(detail);
            try {
                String valueCategory = Constant
                        .ChildConstantDetailTaskCategory.valueOf(categorySelected)
                        .getValueAsLanguage(context);
                view.updateCategory(valueCategory);
            }catch (IllegalArgumentException e){
                Showlog.d("exception category: " + categorySelected);
            }
            try {
                String valuePriority = Constant
                        .ChildConstantDetailTaskPriority.valueOf(prioritySelected)
                        .getValueAsLanguage(context);
                view.updatePriority(valuePriority);
            }catch (IllegalArgumentException e){
                Showlog.d("exception priority: " + prioritySelected);
            }

        }

    }

    @Override
    public void categoryClick() {
        view.showCateory();
    }

    @Override
    public void priorityClick() {
        view.showPriority();
    }

    @Override
    public void saveClick() {
        view.onSave(categorySelected, prioritySelected);
    }

    @Override
    public void onMenuSelect(Context context, int id) {
        switch (id) {
            case R.id.menuCategoryBusiness: {
                view.onSelectCategory(context.getResources().getString(R.string.categoryBusiness));
                categorySelected = Constant.ChildConstantDetailTaskCategory.BUSINESS.getValue();
                break;
            }
            case R.id.menuCategoryPersonal: {
                view.onSelectCategory(context.getResources().getString(R.string.categoryPersonal));
                categorySelected = Constant.ChildConstantDetailTaskCategory.PERSONAL.getValue();
                break;
            }
            case R.id.menuCategoryNone: {
                view.onSelectCategory(context.getResources().getString(R.string.categoryNone));
                categorySelected = Constant.ChildConstantDetailTaskCategory.NONE.getValue();
                break;
            }
            case R.id.menuPriorityHigh: {
                view.onSelectPriority(context.getResources().getString(R.string.priorityHigh));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.HIGH.getValue();
                break;
            }
            case R.id.menuPriorityMedium: {
                view.onSelectPriority(context.getResources().getString(R.string.priorityMedium));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.MEDIUM.getValue();
                break;
            }
            case R.id.menuPriorityLow: {
                view.onSelectPriority(context.getResources().getString(R.string.priorityLow));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.LOW.getValue();
                break;
            }
            case R.id.menuPriorityNone: {
                view.onSelectPriority(context.getResources().getString(R.string.priorityNone));
                prioritySelected = Constant.ChildConstantDetailTaskPriority.NONE.getValue();
                break;
            }
            default: {

            }

        }

    }
}
