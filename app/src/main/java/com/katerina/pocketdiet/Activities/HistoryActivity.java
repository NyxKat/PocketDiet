package com.katerina.pocketdiet.Activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.legacy.app.FragmentPagerAdapter;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.katerina.pocketdiet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Fragments.HistoryExerciseTabFragment;
import Fragments.HistoryNutritionTabFragment;
import Interfaces.HistoryOnClickListener;
import Interfaces.NavigationListener;
import Model.Exercise;
import Model.Nutrition;
import Utils.NonSwipeableViewPager;


/*
HistoryActivity is a screen of Pocket Diet that a user can overview all the diet and workout that he has recorded. This screen shows a UI that includes  TabLayout, NonSwipeableViewPager a custom implementation of
 a View Pager. The ViewPager is used as a view parent of 2 fragments HistoryNutritionTabFragment and HistoryExerciseTabFragment. This screen also provides a filter option for the data on the top right of the menu.

In this screen a user can overview  workouts and meals retrieved from the RealmDatabase grouped by month. User can edit existing data by clicking them. When this action is triggered the NutritionActivity is loaded as the
main UI Screen with all the fields filled from the record the user pressed. Here a user can edit or delete. The same applies by clicking a workout ExerciseActivity is loaded to the Main UI thread.
 */
public class HistoryActivity  extends AppCompatActivity implements HistoryOnClickListener, NavigationListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;
    private Menu menu;

    private AlertDialog dialog;


    private Fragment firstPage, secondPage, thirdPage, fourthPage;
    private HashMap<String, Fragment> components;

    private CalendarDatePickerDialogFragment cdate;
    private String date;

    private ViewPagerAdapter adapter;

    private boolean[] selected_items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initUI();
        init();
    }


    private void initUI(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("History");
         setSupportActionBar(toolbar);


        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_histoty_tab, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        MenuItem menuItem = menu.findItem(R.id.FiltersBasic);

        int tabPosition = tabLayout.getSelectedTabPosition();
        switch (tabPosition) {
            case 0:
                menuItem.setTitle("By food");
                break;
            case 1:
                menuItem.setTitle("By exercise");
                break;
        }


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        switch (id) {
            case R.id.FilterAll:
                switch (tabPosition) {
                    case 0:
                        if (components.containsKey("NutritionTab"))
                            ((HistoryNutritionTabFragment) firstPage).initNutrition();
                        break;
                    case 1:
                        if (components.containsKey("ExerciseTab"))
                            ((HistoryExerciseTabFragment) secondPage).initExercise();
                        break;

                }
                break;

            case R.id.FiltersBasic:
                final EditText edittext = new EditText(getApplicationContext());
                alert.setView(edittext);

                switch (tabPosition) {
                    case 0:
                        alert.setTitle("By food");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                String food = edittext.getText().toString();
                                if (components.containsKey("NutritionTab"))
                                    ((HistoryNutritionTabFragment) firstPage).filterFood(food);

                                dialogInterface.dismiss();
                                dialog.dismiss();
                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                dialog.dismiss();
                            }
                        });

                        break;
                    case 1:
                        alert.setTitle("By exercise");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                String exercise = edittext.getText().toString();
                                if (components.containsKey("ExerciseTab"))
                                    ((HistoryExerciseTabFragment) secondPage).filterExercise(exercise);

                                dialogInterface.dismiss();
                                dialog.dismiss();
                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                dialog.dismiss();
                            }
                        });

                        break;
                }

                dialog = alert.create();
                dialog.show();
                break;



        }

        return false;
    }


    private void init() {

        Calendar calendar = Calendar.getInstance();

        cdate = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(dateSetListener)
                .setDoneText("OK")
                .setCancelText("Cancel")
                .setPreselectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .setThemeLight();

        components = new HashMap<>();

        HistoryNutritionTabFragment historyNutritionTabFragment = new HistoryNutritionTabFragment();
        historyNutritionTabFragment.setHistoryOnClickListener(this);
        historyNutritionTabFragment.setNavigationListener(this);
        components.put("NutritionTab", historyNutritionTabFragment);

        HistoryExerciseTabFragment historyExerciseTabFragment = new HistoryExerciseTabFragment();
        historyExerciseTabFragment.setHistoryOnClickListener(this);
        historyExerciseTabFragment.setNavigationListener(this);
        components.put("ExerciseTab", historyExerciseTabFragment);


        adapter = new ViewPagerAdapter(getFragmentManager());

        adapter.addFragment("Nutrition");
        adapter.addFragment("Exercise");



        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);  // clear all scroll flags

        selected_items = new boolean[1];
        selected_items[0] = true;

    }

    @Override
    public void exerciseClicked(Exercise exercise) {
        Intent intent = new Intent(this,ExerciseActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("exercise",exercise);
        bundle.putBoolean("update",true);
        intent.putExtra("bundle",bundle);

        startActivity(intent);
        finish();

    }

    @Override
    public void nutritionClicked(Nutrition nutrition) {
        Intent intent = new Intent(this,NutritionActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("nutrition",nutrition);
        bundle.putBoolean("update",true);
        intent.putExtra("bundle",bundle);

        startActivity(intent);
        finish();
    }

    @Override
    public void goToNutrition() {
        Intent intent = new Intent(this,NutritionActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToExercice() {
        Intent intent = new Intent(getApplication(), ExerciseActivity.class);
        startActivity(intent);
        finish();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<String> mFragmentTitleList = new ArrayList<>();

        /**
         * @param fm
         * @deprecated Use {@link androidx.fragment.app.FragmentPagerAdapter} instead.
         */
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    firstPage = components.get("NutritionTab");
                    return firstPage;

                case 1:
                    secondPage = components.get("ExerciseTab");
                    return secondPage;

                case 2:
                    thirdPage = components.get("MedicationTab");
                    return thirdPage;
                case 3:
                    fourthPage = components.get("WaterTab");
                    return fourthPage;


            }

            return null;
        }

        @Override
        public int getCount() {
            return mFragmentTitleList.size();
        }

        public void addFragment(String title) {
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            //super.restoreState(state, loader);
        }
    }


    private CalendarDatePickerDialogFragment.OnDateSetListener dateSetListener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
        @Override
        public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
            String month, day;
            monthOfYear++;
            if (monthOfYear < 10)
                month = "0" + monthOfYear;
            else
                month = String.valueOf(monthOfYear);
            if (dayOfMonth < 10)
                day = "0" + dayOfMonth;
            else
                day = String.valueOf(dayOfMonth);
            date = year + "-" + month + "-" + day;


            int tabPosition = tabLayout.getSelectedTabPosition();
            switch (tabPosition) {
                case 0:
                    if (components.containsKey("NutritionTab"))
                        ((HistoryNutritionTabFragment) firstPage).filterDate(date);
                    break;
                case 1:
                    if (components.containsKey("ExerciseTab"))
                        ((HistoryExerciseTabFragment) secondPage).filterDate(date);
                    break;

            }


        }
    };



}
