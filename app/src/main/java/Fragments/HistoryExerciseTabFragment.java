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
import Model.Exercise;
import Model.ExerciseGroupItem;
import Model.ExerciseHistory;
import Model.MonthAndYear;
import Model.RealmDatabase;
import Utils.DividerItemDecoration;
import Utils.TimeManager;
import io.realm.RealmResults;

/*
HistoryExerciseTabFragment is a screen that is used by HistoryActivity to display all the workout information of the user.  This screen shows a UI that includes  RecyclerView which is used as list to display
the workout information. All data are grouped by month. Also a Floating Action Button is displayed at the bottom right of the screen offering navingation to ExerciseActivity if a user wants to add a new workout information.

All data are retrieved from the RealmDatabase and being grouped per year and month on HistoryTabAdapter which is used as the main adapter for the recycler view. Three functions are included on this fragment that are
providing filter options of the data.

 */
public class HistoryExerciseTabFragment extends Fragment {

    private RecyclerView rvHistory;
    private RelativeLayout llNoData;
    private TextView tvNoDataHeader, tvRecord;
    private FloatingActionButton fabAdd;
    private HashSet<MonthAndYear> monthAndYear;

    private RealmDatabase db;

    private HistoryTabAdapter adapter;


    private HistoryOnClickListener historyOnClickListener;
    private NavigationListener navigationListener;
    private ImageView ivNoData;



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
        ivNoData.setImageResource(R.drawable.ic_overview_exercise);
        db = new RealmDatabase(getActivity());

        initExercise();
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

    private void initNoDataFilterExercise(boolean flag) {

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
                navigationListener.goToExercice();
            }
        });

    }


    public void initExercise() {
        List<ExerciseHistory> exerciseHistory = new ArrayList<>();
        ArrayList<ExerciseGroupItem> exerciseGroupItems = new ArrayList<>();
        monthAndYear = new HashSet<>();

        RealmResults<Exercise> allExercise = db.getAllExerciseFromNewestToOldest();
        initNoData(allExercise.isEmpty());

        for (int j = 0; j < allExercise.size(); j++) {
            Exercise child = allExercise.get(j);
            String tokens[] = child.getDate().split("-");
            int year = Integer.parseInt(tokens[0]);
            String month = TimeManager.getMonthFromInt(Integer.parseInt(tokens[1]) - 1);
            MonthAndYear may = new MonthAndYear(month, year);

            if (!monthAndYear.contains(may)) {
                ExerciseGroupItem group = new ExerciseGroupItem(month, year);
                exerciseGroupItems.add(group);
                monthAndYear.add(may);
            }


            for (int i = 0; i < exerciseGroupItems.size(); i++) {
                ExerciseGroupItem item = exerciseGroupItems.get(i);
                if (item.getMonth() == month && item.getYear() == year) {
                    exerciseGroupItems.get(i).addChild(child);
                    break;
                }

            }


        }


        for (int i = 0; i < exerciseGroupItems.size(); i++) {
            ExerciseGroupItem item = exerciseGroupItems.get(i);
            item.setup();
            ExerciseHistory exerciseHistoryGroup = new ExerciseHistory(item.getMonth() + " " + item.getYear(), item.getDistict_childs());
            exerciseHistoryGroup.setInnerItems(item.getGroup_of_childs());
            exerciseHistory.add(exerciseHistoryGroup);

        }

        if(adapter!=null) adapter = null;
        adapter = new HistoryTabAdapter(exerciseHistory, getActivity(), 2,historyOnClickListener);

        rvHistory.setAdapter(adapter);


    }

    private List<ExerciseHistory> initExercise(RealmResults<Exercise> allExercise) {
        List<ExerciseHistory> exerciseHistory = new ArrayList<>();
        ArrayList<ExerciseGroupItem> exerciseGroupItems = new ArrayList<>();
        monthAndYear = new HashSet<>();


        for (int j = 0; j < allExercise.size(); j++) {
            Exercise child = allExercise.get(j);
            String tokens[] = child.getDate().split("-");
            int year = Integer.parseInt(tokens[0]);
            String month = TimeManager.getMonthFromInt(Integer.parseInt(tokens[1]) - 1);
            MonthAndYear may = new MonthAndYear(month, year);

            if (!monthAndYear.contains(may)) {
                ExerciseGroupItem group = new ExerciseGroupItem(month, year);
                exerciseGroupItems.add(group);
                monthAndYear.add(may);
            }


            for (int i = 0; i < exerciseGroupItems.size(); i++) {
                ExerciseGroupItem item = exerciseGroupItems.get(i);
                if (item.getMonth() == month && item.getYear() == year) {
                    exerciseGroupItems.get(i).addChild(child);
                    break;
                }

            }


        }


        for (int i = 0; i < exerciseGroupItems.size(); i++) {
            ExerciseGroupItem item = exerciseGroupItems.get(i);
            item.setup();
            ExerciseHistory exerciseHistoryGroup = new ExerciseHistory(item.getMonth() + " " + item.getYear(), item.getDistict_childs());
            exerciseHistoryGroup.setInnerItems(item.getGroup_of_childs());
            exerciseHistory.add(exerciseHistoryGroup);

        }


        return exerciseHistory;
    }


    public void setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public void setHistoryOnClickListener(HistoryOnClickListener historyOnClickListener){
        this.historyOnClickListener = historyOnClickListener;
    }

    public void filterExercise(String filter) {
        RealmResults<Exercise> exercises = db.getExerciseByName(filter);

        List<ExerciseHistory> nutritionHistory = initExercise(exercises);
        if (adapter != null) {
            adapter = null;
            adapter = new HistoryTabAdapter(nutritionHistory, getActivity(), 2,historyOnClickListener);
            if (rvHistory != null) rvHistory.setAdapter(adapter);
        }

        initNoDataFilterExercise(nutritionHistory.isEmpty());

    }

    public void filterDate(String filter) {
        db = new RealmDatabase(getActivity());
        RealmResults<Exercise> exercises = db.getExerciseByDate(filter);

        List<ExerciseHistory> nutritionHistory = initExercise(exercises);
        if (adapter != null) {
            adapter = null;
            adapter = new HistoryTabAdapter(nutritionHistory, getActivity(), 2,historyOnClickListener);
            if (rvHistory != null) rvHistory.setAdapter(adapter);
        }

        initNoDataFilterDate(nutritionHistory.isEmpty());

    }


}
