package ru.hse.guidehelper.ui.navigationbar.excursion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.excursions.ExcursionsFragment;
import ru.hse.guidehelper.utils.cityautocomplete.PlaceAutoCompleteAdapter;

public final class AllExcursionFragment extends ExcursionsFragment {

    private TextView excursionsCountTextView;
    private TextView excursionCostLimitTextView;
    private AutoCompleteTextView excursionCityFilterTextView;
    private Button excursionFilterAcceptButton;
    private AllTourRecyclerViewAdapter adapter;

    @Override
    protected void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new AllTourRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view == null) {
            return null;
        }

        excursionsCountTextView = view.findViewById(R.id.excursionsCountTextView);
        excursionsCountTextView.setVisibility(View.VISIBLE);
        excursionsCountTextView.setText("Все " + adapter.getToursCount());

        excursionCostLimitTextView = view.findViewById(R.id.excursionCostLimitTextView);
        excursionCostLimitTextView.setText("Цена: 10000 " + Html.fromHtml(" &#x20bd"));

        SeekBar excursionCostSeekBar = view.findViewById(R.id.excursionCostSeekBar);
        excursionCostSeekBar.setProgress(10000);

        CardView sliderCardView = view.findViewById(R.id.overCostSeekBar);
        sliderCardView.setVisibility(View.VISIBLE);

        excursionCostSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int startProgress = 10000;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startProgress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() != startProgress) {
                    adapter.getToursWithCostLimit((long) seekBar.getProgress());
                    excursionsCountTextView.setText("Все " + adapter.getToursCount());
                    excursionCostLimitTextView.setText("Цена: " + seekBar.getProgress() + " " + Html.fromHtml(" &#x20bd"));
                }
            }
        });

        CardView filterCardView = view.findViewById(R.id.overСityFilter);
        filterCardView.setVisibility(View.VISIBLE);

        excursionCityFilterTextView = view.findViewById(R.id.excursionCityFilterTextView);
        excursionCityFilterTextView.setAdapter(new PlaceAutoCompleteAdapter(view.getContext(), android.R.layout.simple_list_item_1));
        excursionFilterAcceptButton = view.findViewById(R.id.excursionFilterAcceptButton);

        excursionFilterAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = excursionCityFilterTextView.getText().toString();
                adapter.getToursByCitySortedByOptionalParameter(cityName);
                excursionsCountTextView.setText("Город \"" + cityName + "\" : " + adapter.getToursCount());

                excursionCityFilterTextView.setText("");
            }
        });

        return view;
    }
}
