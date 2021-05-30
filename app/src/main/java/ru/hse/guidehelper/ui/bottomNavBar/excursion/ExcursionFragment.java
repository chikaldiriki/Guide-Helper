package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;
import ru.hse.guidehelper.model.Tour;


public class ExcursionFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_excursions, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        View recyclerView = root.findViewById(R.id.excursionslist_list);
        assert recyclerView != null;

        setupRecyclerView((RecyclerView) recyclerView);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new TourRecyclerViewAdapter(this));
    }

    public static class TourRecyclerViewAdapter
            extends RecyclerView.Adapter<TourRecyclerViewAdapter.ViewHolder> {

        private final ExcursionFragment mParentActivity;
        private List<Tour> tours = null;
        private static Map<Long, Tour> mapIdTour;

        private final View.OnClickListener mOnClickListener = view -> {
            Tour currTour = (Tour) view.getTag();
            MainActivity.currentTourId = currTour.getId();
            MainActivity.navController.navigate(R.id.excursionsListDetailActivity);
        };

        TourRecyclerViewAdapter(ExcursionFragment parent) {
            mParentActivity = parent;

            tours = RequestHelper.getAllTours();
            mapIdTour = new HashMap<>();
            for(int i = 0; i < tours.size(); i++) {
                Tour tour = tours.get(i);
                mapIdTour.put(tour.getId(), tour);
            }
        }

        public static Tour getTourById(Long id) {
            return mapIdTour.get(id);
        }

        @NotNull
        @Override
        public TourRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.excursions_list_content, parent, false);
            return new TourRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TourRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.title.setText(tours.get(position).getTitle());
            holder.index.setText(String.valueOf(position + 1));
            holder.itemView.setTag(tours.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return tours.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView index;
            final TextView title;

            ViewHolder(View view) {
                super(view);
                index = view.findViewById(R.id.id_text);
                title = view.findViewById(R.id.content);
            }
        }
    }

}
