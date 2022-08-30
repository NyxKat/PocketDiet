package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.katerina.pocketdiet.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import me.grantland.widget.AutofitTextView;

public class HistoryItemHolder extends ChildViewHolder {

    public AutofitTextView tvDate;
    public ViewPager mPager;
    public ImageView ivRightArrow, ivLeftArrow;
    private int currentPos;


    public HistoryItemHolder(View itemView, final Context context) {
        super(itemView);

        tvDate =  itemView.findViewById(R.id.tvDate);
        mPager =  itemView.findViewById(R.id.pager);
        ivRightArrow =  itemView.findViewById(R.id.ivRightArrow);
        ivLeftArrow =  itemView.findViewById(R.id.ivLeftArrow);

        currentPos = mPager.getCurrentItem();

        ivRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = mPager.getCurrentItem();

                int nextPos = (currentPos + 1) % mPager.getAdapter().getCount();
                if (nextPos == 0) ivLeftArrow.setVisibility(View.GONE);
                else ivLeftArrow.setVisibility(View.VISIBLE);

                mPager.setCurrentItem(nextPos);
            }
        });


        ivLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPos = mPager.getCurrentItem();

                int nextPos = currentPos - 1;
                if (nextPos >= 0) {
                    if (nextPos == 0) ivLeftArrow.setVisibility(View.GONE);
                    else ivLeftArrow.setVisibility(View.VISIBLE);

                    mPager.setCurrentItem(nextPos);
                }

            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > currentPos) {
                    int nextPos = (currentPos + 1) % mPager.getAdapter().getCount();
                    if (nextPos == 0) ivLeftArrow.setVisibility(View.GONE);
                    else ivLeftArrow.setVisibility(View.VISIBLE);
                } else {
                    int nextPos = currentPos - 1;
                    if (nextPos >= 0) {
                        if (nextPos == 0) ivLeftArrow.setVisibility(View.GONE);
                        else ivLeftArrow.setVisibility(View.VISIBLE);

                        mPager.setCurrentItem(nextPos);
                    }
                }
                currentPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setup() {
        if (mPager.getAdapter().getCount() == 1) {
            ivLeftArrow.setVisibility(View.GONE);
            ivRightArrow.setVisibility(View.GONE);
        } else {
            ivRightArrow.setVisibility(View.VISIBLE);

            currentPos = mPager.getCurrentItem();
            if (currentPos == 0) ivLeftArrow.setVisibility(View.GONE);
            else ivLeftArrow.setVisibility(View.VISIBLE);
        }


    }


}
