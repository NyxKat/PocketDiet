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
import Model.Nutrition;

public class NutritionScreenSlidePagerAdapter extends PagerAdapter {
    private ArrayList<Nutrition> items;
    private TextView tvMealQuantity, tvMealNumber, tvTime, tvMeal;
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private HistoryOnClickListener historyOnClickListener;

    public NutritionScreenSlidePagerAdapter(Context context, ArrayList<Nutrition> items, HistoryOnClickListener historyOnClickListener) {
        this.items = items;
        // Inflater which will be used for creating all the necessary pages
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.historyOnClickListener = historyOnClickListener;

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View itemView = mLayoutInflater.inflate(R.layout.history_nutrition_item, container, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(historyOnClickListener!=null) historyOnClickListener.nutritionClicked(items.get(position));
            }
        });

        tvMealQuantity =  itemView.findViewById(R.id.tvMealQuantity);
        tvMealNumber =  itemView.findViewById(R.id.tvMealNumber);
        tvTime =  itemView.findViewById(R.id.tvTime);
        tvMeal =  itemView.findViewById(R.id.tvMeal);

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


    private void display(Nutrition nutrition) {
        tvMealQuantity.setTextColor(context.getResources().getColor(R.color.app_gray_darker));
        tvTime.setTextColor(context.getResources().getColor(R.color.app_purple));
        tvMeal.setTextColor(context.getResources().getColor(R.color.app_purple_darker));


        String time,food;
        int current;
        double quantity;

        time = nutrition.getTime();
        food = nutrition.getFood();
        current = nutrition.getCurrent_meal();
        quantity = nutrition.getQuantity();


        String formatedMealNumber;
        String mealNumbers[] = context.getResources().getStringArray(R.array.meal_numbers);
        if (current == 1) {
            formatedMealNumber = mealNumbers[0];
        } else if (current == 2) {
            formatedMealNumber = mealNumbers[1];
        } else if (current == 3) {
            formatedMealNumber = mealNumbers[2];
        } else {
            formatedMealNumber = current + mealNumbers[3];
        }

        tvMealNumber.setText(formatedMealNumber);
        tvTime.setText(time);
        tvMeal.setText(food);
        tvMealQuantity.setText(quantity+"gr");
    }
}
