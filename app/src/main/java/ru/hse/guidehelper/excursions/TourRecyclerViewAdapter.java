package ru.hse.guidehelper.excursions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.model.Tour;

public abstract class TourRecyclerViewAdapter
        extends RecyclerView.Adapter<TourRecyclerViewAdapter.ViewHolder> {

//    public TourRecyclerViewAdapter() {
//        // Required empty public constructor
//    }

    protected List<Tour> tours = null;
    public static Map<Long, Tour> mapIdTour;

    private final View.OnClickListener mOnClickListener = view -> {
        Tour currTour = (Tour) view.getTag();
        MainActivity.currentTourId = currTour.getId();
        MainActivity.navController.navigate(R.id.excursionsListDetailActivity);
    };

    public TourRecyclerViewAdapter() {
        initConstructor();
    }

    protected abstract void initConstructor();

    public static Tour getTourById(Long id) {
        System.out.println("==== getTourById ====");
        System.out.println(mapIdTour.hashCode());
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
        public final TextView index;
        public final TextView title;

        ViewHolder(View view) {
            super(view);
            index = view.findViewById(R.id.id_text);
            title = view.findViewById(R.id.content);
        }
    }
}

