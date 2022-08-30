package Fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.katerina.pocketdiet.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Adapters.HistoryTabAdapter;
import Interfaces.HistoryOnClickListener;
import Interfaces.NavigationListener;
import Model.MonthAndYear;
import Model.Nutrition;
import Model.NutritionGroupItem;
import Model.NutritionHistory;
import Model.RealmDatabase;
import Utils.DividerItemDecoration;
import Utils.TimeManager;
import io.realm.RealmResults;
/*
HistoryNutritionTabFragment is a screen that is used by HistoryActivity to display all the diet information of the user.  This screen shows a UI that includes  RecyclerView which is used as list to display
the diet information. All data are grouped by month. Also a Floating Action Button is displayed at the bottom right of the screen offering navingation to NutritionActivity if a user wants to add a new diet information.

All data are retrieved from the RealmDatabase and being grouped per year and month on HistoryTabAdapter which is used as the main adapter for the recycler view. Three functions are included on this fragment that are
providing filter options of the data.

 */
public class HistoryNutritionTabFragment  extends Fragment {

    private RecyclerView rvHistory;
    private RelativeLayout llNoData;
    private TextView tvNoDataHeader, tvRecord;
    private FloatingActionButton fabAdd;
    private HashSet<MonthAndYear> monthAndYear;
    private ImageView ivNoData;

    private RealmDatabase db;

    private HistoryTabAdapter adapter;
    private HistoryOnClickListener historyOnClickListener;
    private NavigationListener navigationListener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_tab, container, false);

        llNoData =  view.findViewById(R.id.llNoData);
        tvNoDataHeader =  view.findViewById(R.id.tvNoDataHeader);
        tvRecord =  view.findViewById(R.id.tvRecord);
        fabAdd =  view.findViewById(R.id.fabRecord);
        ivNoData =  view.findViewById(R.id.ivNoData);

        rvHistory =  view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHistory.addItemDecoration(new DividerItemDecoration(getActivity().getResources().getDrawable(R.drawable.divider), false, false));


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initListeners();
    }


    private void init() {
        ivNoData.setImageResource(R.drawable.ic_overview_nutrition);
        db = new RealmDatabase(getActivity());

        initNutrition();
    }

    private void initNoData(boolean flag) {
        if (!flag) {
            llNoData.setVisibility(View.GONE);
            ivNoData.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.VISIBLE);
            tvNoDataHeader.setText("No records are registered");
            tvRecord.setText("Record your meal by pressing + button");
            ivNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initNoDataFilterFood(boolean flag) {

        if (!flag) {
            llNoData.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.VISIBLE);
            tvNoDataHeader.setText("No records with specific food");
            tvRecord.setText("Choose All from the button at the top right corner");
        }

    }

    private void initNoDataFilterDate(boolean flag) {

        if (!flag) {
            llNoData.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.VISIBLE);
            tvNoDataHeader.setText("No records with specific date");
            tvRecord.setText("Choose All from the button at the top right corner");
        }

    }

    private void initListeners() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationListener.goToNutrition();
            }
        });

    }

    public void initNutrition() {

        List<NutritionHistory> nutritionHistory = new ArrayList<>();
        ArrayList<NutritionGroupItem> nutritionGroupItems = new ArrayList<>();
        monthAndYear = new HashSet<>();



        RealmResults<Nutrition> allNutrition = db.getAllNutritionFromNewestToOldest();
        initNoData(allNutrition.isEmpty());

        for (int j = 0; j < allNutrition.size(); j++) {
            Nutrition child = allNutrition.get(j);
            String tokens[] = child.getDate().split("-");
            int year = Integer.parseInt(tokens[0]);
            String month = TimeManager.getMonthFromInt(Integer.parseInt(tokens[1]) - 1);
            MonthAndYear may = new MonthAndYear(month, year);

            if (!monthAndYear.contains(may)) {
                NutritionGroupItem group = new NutritionGroupItem(month, year);
                nutritionGroupItems.add(group);
                monthAndYear.add(may);
            }


            for (int i = 0; i < nutritionGroupItems.size(); i++) {
                NutritionGroupItem item = nutritionGroupItems.get(i);
                if (item.getMonth() == month && item.getYear() == year) {
                    nutritionGroupItems.get(i).addChild(child);
                    break;
                }

            }


        }


        for (int i = 0; i < nutritionGroupItems.size(); i++) {
            NutritionGroupItem item = nutritionGroupItems.get(i);
            item.setup();
            NutritionHistory nutritionHistoryGroup = new NutritionHistory(item.getMonth() + " " + item.getYear(), item.getDistict_childs());
            nutritionHistoryGroup.setInnerItems(item.getGroup_of_childs());
            nutritionHistory.add(nutritionHistoryGroup);

        }


        if(adapter!= null) adapter = null;
        adapter = new HistoryTabAdapter(nutritionHistory, getActivity(), 1,historyOnClickListener);

        rvHistory.setAdapter(adapter);
    }


    private List<NutritionHistory> initNutrition(RealmResults<Nutrition> allNutrition) {

        List<NutritionHistory> nutritionHistory = new ArrayList<>();
        ArrayList<NutritionGroupItem> nutritionGroupItems = new ArrayList<>();
        monthAndYear = new HashSet<>();


        for (int j = 0; j < allNutrition.size(); j++) {
            Nutrition child = allNutrition.get(j);
            String tokens[] = child.getDate().split("-");
            int year = Integer.parseInt(tokens[0]);
            String month = TimeManager.getMonthFromInt(Integer.parseInt(tokens[1]) - 1);
            MonthAndYear may = new MonthAndYear(month, year);

            if (!monthAndYear.contains(may)) {
                NutritionGroupItem group = new NutritionGroupItem(month, year);
                nutritionGroupItems.add(group);
                monthAndYear.add(may);
            }


            for (int i = 0; i < nutritionGroupItems.size(); i++) {
                NutritionGroupItem item = nutritionGroupItems.get(i);
                if (item.getMonth() == month && item.getYear() == year) {
                    nutritionGroupItems.get(i).addChild(child);
                    break;
                }

            }


        }


        for (int i = 0; i < nutritionGroupItems.size(); i++) {
            NutritionGroupItem item = nutritionGroupItems.get(i);
            item.setup();
            NutritionHistory nutritionHistoryGroup = new NutritionHistory(item.getMonth() + " " + item.getYear(), item.getDistict_childs());
            nutritionHistoryGroup.setInnerItems(item.getGroup_of_childs());
            nutritionHistory.add(nutritionHistoryGroup);

        }

        return nutritionHistory;

    }


    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }


    public void setHistoryOnClickListener(HistoryOnClickListener historyOnClickListener){
        this.historyOnClickListener = historyOnClickListener;
    }


    public void filterFood(String filter) {
        RealmResults<Nutrition> nutritions = db.getNutritionByFood(filter);

        List<NutritionHistory> nutritionHistory = initNutrition(nutritions);
        if (adapter != null) {
            adapter = null;
            adapter = new HistoryTabAdapter(nutritionHistory, getActivity(), 1,historyOnClickListener);
            if (rvHistory != null) rvHistory.setAdapter(adapter);
        }

        initNoDataFilterFood(nutritionHistory.isEmpty());

    }

    public void filterDate(String filter){
        RealmResults<Nutrition> nutritions = db.getNutritionByDate(filter);

        List<NutritionHistory> nutritionHistory = initNutrition(nutritions);
        if (adapter != null) {
            adapter = null;
            adapter = new HistoryTabAdapter(nutritionHistory, getActivity(), 1,historyOnClickListener);
            if (rvHistory != null) rvHistory.setAdapter(adapter);
        }

        initNoDataFilterDate(nutritionHistory.isEmpty());
    }


}
