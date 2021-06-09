package ru.hse.guidehelper.excursions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Base64;
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

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public static void setMapIdTour(Map<Long, Tour> mapIdTour) {
        TourRecyclerViewAdapter.mapIdTour = mapIdTour;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public static Map<Long, Tour> getMapIdTour() {
        return mapIdTour;
    }

    public TourRecyclerViewAdapter(List<Tour> tours) {
        initConstructor(tours);
    }

    protected abstract void initConstructor(List<Tour> tours);

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
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final TourRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.excursionName.setText(tours.get(position).getTitle());

        if (tours.get(position).getImage() != null) {
            byte[] imageByteArray = Base64.getDecoder().decode(tours.get(position).getImage());
            Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            holder.imageImageView.setImageBitmap(image);
        } else {
            holder.imageImageView.setImageResource(R.drawable.ic_launcher_background);
        }
        String cost = tours.get(position).getCost().toString() + ' ' + Html.fromHtml(" &#x20bd");

        holder.costTextView.setText(cost);
        holder.itemView.setTag(tours.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView excursionName;
        public final TextView costTextView;
        public final ImageView imageImageView;

        ViewHolder(View view) {
            super(view);
            excursionName = view.findViewById(R.id.id_excursionNameTextView);
            costTextView = view.findViewById(R.id.excursionsCostTextView);
            imageImageView = view.findViewById(R.id.excursionsImageImageView);
        }
    }
}

