package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.katerina.pocketdiet.R;

import java.util.ArrayList;

import Interfaces.HistoryOnClickListener;
import Model.Exercise;

public class ExerciseScreenSlidePagerAdapter extends PagerAdapter {
    private ArrayList<Exercise> items;
    private TextView tvExercise, tvPeriod, tvTime, tvWorkTime;
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private HistoryOnClickListener historyOnClickListener;


    public ExerciseScreenSlidePagerAdapter(Context context, ArrayList<Exercise> items, HistoryOnClickListener historyOnClickListener) {
        this.items = items;
        // Inflater which will be used for creating all the necessary pages
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.historyOnClickListener = historyOnClickListener;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.history_exercise_item, container, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyOnClickListener != null) {
                    historyOnClickListener.exerciseClicked(items.get(position));
                }
            }
        });

        tvExercise =  itemView.findViewById(R.id.tvExercise);
        tvPeriod =  itemView.findViewById(R.id.tvExercisePeriod);
        tvTime =  itemView.findViewById(R.id.tvTime);
        tvWorkTime =  itemView.findViewById(R.id.tvExerciseWorkTime);

        display(items.get(position));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Removes the page from the container for the given position. We simply removed object using removeView()
        // but could’ve also used removeViewAt() by passing it the position.
        try {
            // Remove the view from the container
            container.removeView((View) object);

            object = null;
        } catch (Exception e) {
            Log.w("Mhstos", "destroyItem: failed to destroy item and clear it's used resources", e);
        }
    }


    private void display(Exercise exerciseItem) {
        String exercise, period, time;
        double workTime;

        time = exerciseItem.getTime();
        exercise = exerciseItem.getExercise();
        //period = exerciseItem.getPeriodOfDay();
        workTime = exerciseItem.getWorkTime();


        tvExercise.setText(exercise);
        tvTime.setText(time);
        tvWorkTime.setText(workTime + "");
        //tvPeriod.setText(period);
    }

}
