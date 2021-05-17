package ru.hse.guidehelper.excursions;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.hse.guidehelper.R;

public class ExcursionsListDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private ExcursionsListActivity.SimpleItemRecyclerViewAdapter.DummyItem mItem;

    public ExcursionsListDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            mItem = ExcursionsListActivity.SimpleItemRecyclerViewAdapter.itemMap.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.excursionslist_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.excursionslist_detail)).setText(mItem.details);
        }

        return rootView;
    }
}