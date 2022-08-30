package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.katerina.pocketdiet.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class HistoryGroupHolder extends GroupViewHolder {

    public TextView tvGroupName;
    public ImageView ivArrow;

    public HistoryGroupHolder(View itemView, Context context) {
        super(itemView);

        tvGroupName =  itemView.findViewById(R.id.tvHeader);
        ivArrow =  itemView.findViewById(R.id.ivArrow);
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        ivArrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        ivArrow.setAnimation(rotate);
    }
}
