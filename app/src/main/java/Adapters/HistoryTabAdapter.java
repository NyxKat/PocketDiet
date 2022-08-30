package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katerina.pocketdiet.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableList;

import java.util.ArrayList;
import java.util.List;

import Interfaces.HistoryOnClickListener;
import Model.Exercise;
import Model.ExerciseHistory;
import Model.Nutrition;
import Model.NutritionHistory;
import Utils.TimeManager;

public class HistoryTabAdapter extends ExpandableRecyclerViewAdapter<HistoryGroupHolder, HistoryItemHolder> {
    private Context context;
    private int indicator;
    private HistoryOnClickListener historyOnClickListener;
    private boolean isTotalView;

    public HistoryTabAdapter(List<? extends ExpandableGroup> groups, Context context, int indicator, HistoryOnClickListener historyOnClickListener) {
        super(groups);
        this.context = context;
        this.indicator = indicator;
        this.historyOnClickListener = historyOnClickListener;
    }

    public void setHistoryOnClickListener(HistoryOnClickListener historyOnClickListener){
        this.historyOnClickListener = historyOnClickListener;
    }

    public void setTotalView(boolean totalView){
        this.isTotalView  = totalView;
    }

    @Override
    public HistoryGroupHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_group_item, parent, false);
        return new HistoryGroupHolder(view,context);
    }

    @Override
    public HistoryItemHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_child, parent, false);
        return new HistoryItemHolder(view,context);
    }

    @Override
    public void onBindChildViewHolder(HistoryItemHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        switch (indicator){
            case 1:
                ArrayList<Nutrition> items = ((NutritionHistory) group).getInnerItems().get(childIndex);
                if(!items.isEmpty()) {
                    String formatedDate = TimeManager.getMonthAndDayFromDate(items.get(0).getDate());
                    holder.tvDate.setText(formatedDate);
                }

                NutritionScreenSlidePagerAdapter screenSlidePagerAdapter = new NutritionScreenSlidePagerAdapter(context,items,historyOnClickListener);
                holder.mPager.setAdapter(screenSlidePagerAdapter);
                holder.setup();
                break;
            case 2:
                ArrayList<Exercise> exerciseItems = ((ExerciseHistory) group).getInnerItems().get(childIndex);
                if(!exerciseItems.isEmpty()) {
                    String formatedDate = TimeManager.getMonthAndDayFromDate(exerciseItems.get(0).getDate());
                    holder.tvDate.setText(formatedDate);
                }

                ExerciseScreenSlidePagerAdapter exerciseScreenSlidePagerAdapter = new ExerciseScreenSlidePagerAdapter(context,exerciseItems,historyOnClickListener);
                holder.mPager.setAdapter(exerciseScreenSlidePagerAdapter);
                holder.setup();
                break;


        }


    }

    @Override
    public void onBindGroupViewHolder(HistoryGroupHolder holder, int flatPosition, ExpandableGroup group) {
        holder.tvGroupName.setText(group.getTitle());
    }


    public void notifyList(List<? extends ExpandableGroup> groups){
        this.expandableList = new ExpandableList(groups);

        notifyDataSetChanged();
    }
}
