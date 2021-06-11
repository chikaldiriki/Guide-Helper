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

import ru.hse.guidehelper.MainActivity;
import ru.hse.guidehelper.R;
import ru.hse.guidehelper.model.Tour;
import ru.hse.guidehelper.model.TourOrder;

public abstract class TourRecyclerViewAdapter<T extends Tour>
        extends RecyclerView.Adapter<TourRecyclerViewAdapter.ViewHolder> {

    protected List<T> tours = null;

    private final View.OnClickListener mOnClickListener = view -> {
        T currTour = (T) view.getTag();
        MainActivity.currentTourId = currTour.getId();
        MainActivity.navController.navigate(R.id.excursionsListDetailActivity);
    };

    public void setTours(List<T> tours) {
        this.tours = tours;
    }

    public List<T> getTours() {
        return tours;
    }

    public TourRecyclerViewAdapter(List<T> tours) {
        initConstructor(tours);
    }

    protected abstract void initConstructor(List<T> tours);

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
        String city = tours.get(position).getCity().replaceFirst(",[a-zA-Zа-яА-Я -,]*", "");

        holder.costTextView.setText(cost);
        holder.cityTextView.setText(city);
        holder.itemView.setTag(tours.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);

        if(tours.get(position).getClass() == TourOrder.class) {
            holder.dateOfTourTextView.setText(((TourOrder)tours.get(position)).getDate());
        } else if(tours.get(position).getClass() == Tour.class) {
            holder.dateOfTourTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView excursionName;
        public final TextView costTextView;
        public final TextView cityTextView;
        public final TextView dateOfTourTextView;
        public final ImageView imageImageView;

        ViewHolder(View view) {
            super(view);
            excursionName = view.findViewById(R.id.id_excursionNameTextView);
            costTextView = view.findViewById(R.id.excursionsCostTextView);
            cityTextView = view.findViewById(R.id.excursionsCityTextView);
            dateOfTourTextView = view.findViewById(R.id.excursionsDateOfOrderTextView);
            imageImageView = view.findViewById(R.id.excursionsImageImageView);
        }
    }
}

