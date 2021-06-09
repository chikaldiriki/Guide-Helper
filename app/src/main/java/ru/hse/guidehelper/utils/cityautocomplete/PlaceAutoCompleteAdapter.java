package ru.hse.guidehelper.utils.cityautocomplete;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

public class PlaceAutoCompleteAdapter extends ArrayAdapter implements Filterable {

    private ArrayList<String> results;

    private final PlacesAPI placesAPI = new PlacesAPI();

    public PlaceAutoCompleteAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public String getItem(int position) {
        return results.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {
                    results = placesAPI.autoComplete(constraint.toString());

                    filterResults.values = results;
                    filterResults.count = results.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results1) {
                if (results1 != null && results1.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
