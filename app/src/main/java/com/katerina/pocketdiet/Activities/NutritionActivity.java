package com.katerina.pocketdiet.Activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.katerina.pocketdiet.R;
import com.tiper.MaterialSpinner;
import java.util.Calendar;

import Adapters.AutoCompleteTextViewAdapter;
import Interfaces.DateTimeListener;
import Model.AutoCompleteNutrition;
import Model.Nutrition;
import Model.NutritionCalories;
import Model.NutritionExtra;
import Model.RealmDatabase;
import Utils.TimeManager;
import me.grantland.widget.AutofitTextView;

/*
NutritionActivity is the main screen of Pocket Diet that a user can record his diet. This screen shows a UI that includes Spinner, ImageView, EditText components inside CardView components.
Third-parties libraries are used on this activity such as MaterialSpinner a lightweight material spinner, BetterPickerLibrary which offers UI for calendar and clock.

In this screen a user can record a daily meal, providing information such as calories, fat, protein , meal type, date and time. All this information are saved to the RealmDatabase and can be
previewed from the History Screen.
 */
public class NutritionActivity extends AppCompatActivity implements DateTimeListener {
    private Toolbar toolbar;
    private TextView tvHeader;
    private AutofitTextView tvDate, tvTime;
    private AutoCompleteTextView actvFood;
    private EditText etBread, etSalad, etSweet, etAvgCalories, etCarbs, etFat, etProtein;
    private ImageView ivNutrition;
    private ImageButton ibDate, ibTime;
    private MaterialSpinner sSelectionMeals;

    private CardView cvFoodSelection, cvMeal;

    private CalendarDatePickerDialogFragment cdate;
    private RadialTimePickerDialogFragment rtime;

    private String date, time;
    private boolean updateData;
    private int id;

    private double salad, bread, sweets;
    private double calories, fat, protein, carbs;

    private AutoCompleteTextViewAdapter autoCompleteTextViewAdapter;

    private RealmDatabase db;
    private Nutrition nutrition;

    private InputMethodManager inputMethodManagerFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
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
                addNutrition();
                break;
            case R.id.action_Delete:
                deleteNutrition();
                break;

        }

        return false;
    }

    private void addNutrition() {

        if (actvFood.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter the food", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!etBread.getText().toString().trim().equals("")) {
            bread = Double.parseDouble(etBread.getText().toString());
        }
        if (!etSalad.getText().toString().trim().equals("")) {
            salad = Double.parseDouble(etSalad.getText().toString());
        }
        if (!etSweet.getText().toString().trim().equals("")) {
            sweets = Double.parseDouble(etSweet.getText().toString());
        }

        NutritionExtra extra = new NutritionExtra(bread, salad, sweets);


        if (!etAvgCalories.getText().toString().trim().equals("")) {
            calories = Double.parseDouble(etAvgCalories.getText().toString());
        }
        if (!etCarbs.getText().toString().trim().equals("")) {
            carbs = Double.parseDouble(etCarbs.getText().toString());
        }
        if (!etFat.getText().toString().trim().equals("")) {
            fat = Double.parseDouble(etFat.getText().toString());
        }
        if (!etProtein.getText().toString().trim().equals("")) {
            protein = Double.parseDouble(etProtein.getText().toString());
        }

        NutritionCalories nutritionCalories = new NutritionCalories(carbs, calories, fat, protein);


        nutrition = new Nutrition(actvFood.getText().toString(), 10, time, date, db.getCurrentMealNumber(date), extra, sSelectionMeals.getSelectedItem().toString(), nutritionCalories);
        if (id == -1) nutrition.setNutritionID(db.getNutritionNextKey());
        else nutrition.setNutritionID(id);


        AutoCompleteNutrition autoCompleteNutrition = new AutoCompleteNutrition(actvFood.getText().toString().toLowerCase());

        db.insertOrUpdateNutrition(nutrition);
        db.insertAutoCompleteNutrition(autoCompleteNutrition);
        db.getAllNutritionFromNewestToOldest();

        closeKeyboard();

        finish();

    }


    private void deleteNutrition() {
        if (id <= 0) return;
        else {
            db.deleteNutrition(id);
            closeKeyboard();
            finish();
        }
    }


    private void initUI() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.app_gray));
        }

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitle("Nutrition");

        cvFoodSelection = findViewById(R.id.cvFoodSelection);
        cvMeal = findViewById(R.id.cvMeal);

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvHeader = findViewById(R.id.tvHeader);


        actvFood = findViewById(R.id.etFood);
        etBread = findViewById(R.id.etBread);
        etSalad = findViewById(R.id.etSalad);
        etSweet = findViewById(R.id.etSweet);
        etAvgCalories = findViewById(R.id.etCalories);
        etCarbs = findViewById(R.id.etCarbs);
        etFat = findViewById(R.id.etFat);
        etProtein = findViewById(R.id.etProtein);

        ivNutrition = findViewById(R.id.rivNutrition);


        ibDate = findViewById(R.id.ibDate);
        ibTime = findViewById(R.id.ibTime);

        sSelectionMeals = findViewById(R.id.sSelectionMeals);


        ibDate.setColorFilter(getResources().getColor(R.color.app_gray), PorterDuff.Mode.SRC_IN);
        ibTime.setColorFilter(getResources().getColor(R.color.app_gray), PorterDuff.Mode.SRC_IN);


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

    }

    private void init() {

        Bundle bundle = getIntent().getBundleExtra("bundle");

        if (bundle != null) {

            nutrition = bundle.getParcelable("nutrition");
            updateData = bundle.getBoolean("update");

        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meals, R.layout.spinner_item_diet);
        sSelectionMeals.setAdapter(adapter);

        inputMethodManagerFood = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        db = new RealmDatabase(this);

        autoCompleteTextViewAdapter = new AutoCompleteTextViewAdapter(this, 1);
        actvFood.setAdapter(autoCompleteTextViewAdapter);


        salad = bread = sweets = -1;
        fat = protein = calories = carbs = -1;


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
                .setThemeCustom(R.style.MyCustomBetterPickersDialogs);


        setupDiet();

    }


    private void setupDiet() {

        if (!updateData) {
            time = TimeManager.getCurrentTime();
            date = TimeManager.getCurrentDate();
            id = -1;

            tvDate.setText("Today");
            tvTime.setText(time);

            sSelectionMeals.post(new Runnable() {
                @Override
                public void run() {
                    sSelectionMeals.setSelection(0);

                }
            });

            actvFood.post(new Runnable() {
                @Override
                public void run() {
                    actvFood.setText("");
                }
            });
            etBread.post(new Runnable() {
                @Override
                public void run() {
                    etBread.setText("");
                }
            });
            etSalad.post(new Runnable() {
                @Override
                public void run() {
                    etSalad.setText("");
                }
            });
            etSweet.post(new Runnable() {
                @Override
                public void run() {
                    etSweet.setText("");
                }
            });

            etFat.post(new Runnable() {
                @Override
                public void run() {
                    etFat.setText("");
                }
            });

            etProtein.post(new Runnable() {
                @Override
                public void run() {
                    etProtein.setText("");
                }
            });


            etAvgCalories.post(new Runnable() {
                @Override
                public void run() {
                    etAvgCalories.setText("");
                }
            });

            etCarbs.post(new Runnable() {
                @Override
                public void run() {
                    etCarbs.setText("");
                }
            });


        } else {
            if (nutrition != null) {
                actvFood.post(new Runnable() {
                    @Override
                    public void run() {
                        actvFood.setText(nutrition.getFood());
                    }
                });

                date = nutrition.getDate();
                time = nutrition.getTime();
                id = nutrition.getNutritionID();

                if (date.equals(TimeManager.getCurrentDate()))
                    tvDate.setText("Today");
                else tvDate.setText(date);
                tvTime.setText(time);

                sSelectionMeals.post(new Runnable() {
                    @Override
                    public void run() {
                        String meals[] = getResources().getStringArray(R.array.meals);
                        int position = -1;

                        if (nutrition.getType_of_meal().equals(meals[0])) {
                            position = 0;
                        } else if (nutrition.getType_of_meal().equals(meals[1])) {
                            position = 1;
                        } else if (nutrition.getType_of_meal().equals(meals[2])) {
                            position = 2;
                        } else {
                            position = 3;
                        }
                        sSelectionMeals.setSelection(position);

                    }
                });

                bread = nutrition.getNutritionExtra().getBread();
                salad = nutrition.getNutritionExtra().getSalad();
                sweets = nutrition.getNutritionExtra().getSweets();

                fat = nutrition.getNutritionCalories().getFat();
                carbs = nutrition.getNutritionCalories().getCarbs();
                protein = nutrition.getNutritionCalories().getProtein();
                calories = nutrition.getNutritionCalories().getCalories();


                etBread.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bread != -1) etBread.setText(bread + "");
                        else etBread.setText("");
                    }
                });

                etSalad.post(new Runnable() {
                    @Override
                    public void run() {
                        if (salad != -1) etSalad.setText(salad + "");
                        else etSalad.setText("");
                    }
                });


                etSweet.post(new Runnable() {
                    @Override
                    public void run() {
                        if (sweets != -1) etSweet.setText(sweets + "");
                        else etSweet.setText("");
                    }
                });

                etFat.post(new Runnable() {
                    @Override
                    public void run() {
                        if (fat != -1) etFat.setText(fat + "");
                        else etFat.setText("");
                    }
                });

                etProtein.post(new Runnable() {
                    @Override
                    public void run() {
                        if (protein != -1) etProtein.setText(protein + "");
                        else etProtein.setText("");
                    }
                });


                etAvgCalories.post(new Runnable() {
                    @Override
                    public void run() {
                        if (calories != -1) etAvgCalories.setText(calories + "");
                        else etAvgCalories.setText("");
                    }
                });

                etCarbs.post(new Runnable() {
                    @Override
                    public void run() {
                        if (carbs != -1) etCarbs.setText(carbs + "");
                        else etCarbs.setText("");
                    }
                });


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
        if (inputMethodManagerFood.isActive())
            inputMethodManagerFood.hideSoftInputFromWindow(actvFood.getWindowToken(), 0);
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
