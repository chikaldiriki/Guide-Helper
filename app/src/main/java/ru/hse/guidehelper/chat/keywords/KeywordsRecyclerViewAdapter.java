package ru.hse.guidehelper.chat.keywords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;

public class KeywordsRecyclerViewAdapter extends
        RecyclerView.Adapter<KeywordsRecyclerViewAdapter.ViewHolder> {

    private List<String> keywords;

    public KeywordsRecyclerViewAdapter(String firstUser, String secondUser) {
        keywords = RequestHelper.getKeywords(firstUser, secondUser);
        if (keywords.isEmpty()) {
            keywords = Collections.singletonList("No keywords ((");
        }
    }

    @NonNull
    @NotNull
    @Override
    public KeywordsRecyclerViewAdapter.ViewHolder onCreateViewHolder(
            @NonNull @NotNull ViewGroup parent, int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keyword_item, parent, false);
        return new KeywordsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull KeywordsRecyclerViewAdapter.ViewHolder holder,
                                 int position) {
        holder.keyword.setText(keywords.get(position));
        holder.itemView.setTag(keywords.get(position));

    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView keyword;

        ViewHolder(View view) {
            super(view);
            keyword = view.findViewById(R.id.id_keyword);
        }
    }
}
