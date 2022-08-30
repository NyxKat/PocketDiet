package com.katerina.pocketdiet.Activities;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.katerina.pocketdiet.R;
import com.michael.easydialog.EasyDialog;

import java.util.Calendar;


import Adapters.AutoCompleteTextViewAdapter;
import Interfaces.DateTimeListener;
import Model.AutoCompleteExercise;
import Model.Exercise;
import Model.RealmDatabase;
import Utils.TimeManager;
import me.grantland.widget.AutofitTextView;

/*
ExerciseActivity is the main screen of Pocket Diet that a user can record his workout habits. This screen shows a UI that includes  ImageView, EditText components inside CardView components.
Third-parties libraries are used on this activity such as BetterPickerLibrary which offers UI for calendar and clock.

In this screen a user can record a daily workout, providing information such as type of exercise and workout time. All this information are saved to the RealmDatabase and can be
previewed from the History Screen.
 */
public class ExerciseActivity extends AppCompatActivity implements DateTimeListener {


    private Toolbar toolbar;
    private TextView tvExercise, tvWorkTime, tvHeader;
    private AutofitTextView tvDate, tvTime;
    private AutoCompleteTextView actvExercise;
    private EditText etWorkTime;
    private ImageView ivExercise;
    private ImageButton ibDate, ibTime;
    private ImageView ivTip;
    private CardView cvWorktime, cvExercise;

    private RealmDatabase db;

    private CalendarDatePickerDialogFragment cdate;
    private RadialTimePickerDialogFragment rtime;

    private String date, time;
    private boolean updateData;
    private int id;

    private AutoCompleteTextViewAdapter adapter;

    private InputMethodManager inputMethodManagerExercise, inputMethodManagerWorkoutTime;

    private Exercise exercise;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        initUI();
        init();
        initListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nutrition, menu);
        if (!updateData) {
            MenuItem menuItem = menu.findItem(R.id.action_Delete);
            menuItem.setVisible(false);
            invalidateOptionsMenu();
        }
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_Done:
                Log.e("Done", "Pressed");
                addExercise();
                break;


            case R.id.action_Delete:
                deleteExercise();
                break;
        }

        return false;
    }


    private void addExercise() {

        if (actvExercise.getText().toString().trim().equals("")) {
            Toast.makeText(this,"Please enter the exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etWorkTime.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter the workout time", Toast.LENGTH_SHORT).show();
            return;
        }

        exercise = new Exercise(actvExercise.getText().toString(), date, time, Double.valueOf(etWorkTime.getText().toString()));
        //If the user inserts data for first time create a new Exercise object and set a new id. Then insert it to the database
        if (id == -1) {
            exercise.setId(db.getExerciseNextKey());
        } else {
            //Otherwise the user is updating this exercise so set to the current exercise object the new values if they exist
            exercise.setId(id);
        }

        AutoCompleteExercise autoCompleteExercise = new AutoCompleteExercise(actvExercise.getText().toString().toLowerCase());

        db.insertOrUpdateExercise(exercise);
        db.insertAutoCompleteExercise(autoCompleteExercise);
        db.getAllExercise();

        closeKeyboard();

        finish();
    }

    private void deleteExercise() {
        if (id <= 0) return;
        else {
            db.deleteExercise(id);
            closeKeyboard();

            finish();
        }
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Workout");
        setSupportActionBar(toolbar);


        tvExercise = findViewById(R.id.tvExercise);
        tvWorkTime = findViewById(R.id.tvWorkTime);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvHeader = findViewById(R.id.tvHeader);


        actvExercise = findViewById(R.id.etExercise);
        etWorkTime = findViewById(R.id.etWorkTime);

        ivExercise = findViewById(R.id.rivExercise);

        ibDate = findViewById(R.id.ibDate);
        ibTime = findViewById(R.id.ibTime);

        ivTip = findViewById(R.id.ivTip);

        cvWorktime = findViewById(R.id.cvWorkTime);
        cvExercise = findViewById(R.id.cvExercise);

    }


    private void initListeners() {


        ibDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDateDialog(cdate);
            }
        });


        ibTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showTimeDialog(rtime);
            }
        });


        ivTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View view =inflater.inflate(R.layout.layout_tip, null);

                TextView tvTip = (TextView) view.findViewById(R.id.tvTip);
                tvTip.setText("Workout time is measured on minutes");

                new EasyDialog(getApplicationContext())
                        .setLayout(view)
                        .setBackgroundColor(getResources().getColor(R.color.app_green))
                        .setLocationByAttachedView(cvWorktime)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                        .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_Y, 1000, -800, 100, -50, 50, 0)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_Y, 500, 0, -800)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .show();


            }
        });
    }

    private void init() {
        Bundle bundle = getIntent().getBundleExtra("bundle");

        if (bundle != null) {
            exercise = bundle.getParcelable("exercise");
            updateData = bundle.getBoolean("update");

        }


        inputMethodManagerExercise = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManagerWorkoutTime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        adapter = new AutoCompleteTextViewAdapter(getApplicationContext(), 2);
        actvExercise.setAdapter(adapter);

        db = new RealmDatabase(this);


        Calendar calendar = Calendar.getInstance();

        cdate = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(dateSetListener)
                .setDoneText("OK")
                .setCancelText("Cancel")
                .setPreselectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .setThemeLight();


        rtime = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(timeSetListener)
                .setDoneText("OK")
                .setCancelText("Cancel")
                .setStartTime(calendar.getTime().getHours(), calendar.getTime().getMinutes())
                .setThemeLight();


        if (!updateData) {
            time = TimeManager.getCurrentTime();
            date = TimeManager.getCurrentDate();
            id = -1;


            actvExercise.post(new Runnable() {
                @Override
                public void run() {
                    actvExercise.setText("");
                }
            });

            etWorkTime.post(new Runnable() {
                @Override
                public void run() {
                    etWorkTime.setText("");
                }
            });
            tvDate.setText("Today");
            tvTime.setText(time);

        } else {
            if (exercise != null) {
                actvExercise.setVisibility(View.VISIBLE);
                etWorkTime.setVisibility(View.VISIBLE);
                ivTip.setVisibility(View.VISIBLE);

                tvExercise.setVisibility(View.GONE);
                tvWorkTime.setVisibility(View.GONE);

                actvExercise.post(new Runnable() {
                    @Override
                    public void run() {
                        actvExercise.setText(exercise.getExercise());
                    }
                });


                etWorkTime.post(new Runnable() {
                    @Override
                    public void run() {
                        etWorkTime.setText(exercise.getWorkTime() + "");
                    }
                });

                date = exercise.getDate();
                time = exercise.getTime();
                id = exercise.getId();


                if (date.equals(TimeManager.getCurrentDate()))
                    tvDate.setText("Today");
                else tvDate.setText(date);

                tvTime.setText(time);


            }
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


            if (TimeManager.isToday(date)) {
                tvDate.setText("Today");
            } else if (TimeManager.isYesterday(date)) {
                tvDate.setText("Yesterday");
            } else {
                tvDate.setText(date);
            }


        }
    };

    private RadialTimePickerDialogFragment.OnTimeSetListener timeSetListener = new RadialTimePickerDialogFragment.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
            String hours, mins;
            if (hourOfDay < 10)
                hours = "0" + hourOfDay;
            else
                hours = String.valueOf(hourOfDay);
            if (minute < 10)
                mins = "0" + minute;
            else
                mins = String.valueOf(minute);
            time = hours + ":" + mins;

            tvTime.setText(time);

        }
    };


    public void closeKeyboard() {
        if (inputMethodManagerExercise.isActive())
            inputMethodManagerExercise.hideSoftInputFromWindow(actvExercise.getWindowToken(), 0);
        else if (inputMethodManagerWorkoutTime.isActive())
            inputMethodManagerWorkoutTime.hideSoftInputFromWindow(etWorkTime.getWindowToken(), 0);
    }


    @Override
    public void showTimeDialog(RadialTimePickerDialogFragment fragment) {
        fragment.show(getSupportFragmentManager(), "TimeFragment");
    }

    @Override
    public void showDateDialog(CalendarDatePickerDialogFragment fragment) {
        fragment.show(getSupportFragmentManager(), "DateFragment");
    }
}
