package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.katerina.pocketdiet.R;

import java.util.ArrayList;

import Model.RealmDatabase;

public class AutoCompleteTextViewAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private RealmDatabase db;
    private ArrayList<String> resultList;
    private int indicator;

    public AutoCompleteTextViewAdapter(Context context,int indicator) {
        this.context = context;
        this.indicator = indicator;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.autocomplete_textview_item, parent, false);

        TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);

        tvHeader.setText(resultList.get(position));
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    if (constraint.length() > 2) {
                        String beginswith = constraint.toString().toLowerCase();

                        db = new RealmDatabase(context);
                        if(indicator == 1) resultList = db.getAutoCompleteNutritionBasedOnFirst2Characters(beginswith);
                        else if(indicator == 2) resultList = db.getAutoCompleteExerciseBasedOnFirst2Characters(beginswith);


                        if (!resultList.isEmpty()) {
                            filterResults.values = resultList;
                            filterResults.count = resultList.size();
                        }

                    }

                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }


        };

        return filter;
    }
}
