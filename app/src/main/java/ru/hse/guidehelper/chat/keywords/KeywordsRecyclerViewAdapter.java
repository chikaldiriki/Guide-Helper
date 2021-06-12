package ru.hse.guidehelper.chat.keywords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;

public class KeywordsRecyclerViewAdapter extends
        RecyclerView.Adapter<KeywordsViewHolder> {

    private List<String> keywords;

    public KeywordsRecyclerViewAdapter(String firstUser, String secondUser) {
        keywords = RequestHelper.getKeywords(firstUser, secondUser);
        if (keywords == null) {
            keywords = Collections.singletonList("Something went wrong..");
        }
        if (keywords.isEmpty()) {
            keywords = Collections.singletonList("No keywords ((");
        }
    }

    @NonNull
    @NotNull
    @Override
    public KeywordsViewHolder onCreateViewHolder(
            @NonNull @NotNull ViewGroup parent, int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keyword_item, parent, false);
        return new KeywordsViewHolder(view, R.id.id_keyword);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull KeywordsViewHolder holder,
                                 int position) {
        holder.keyword.setText(keywords.get(position));
        holder.itemView.setTag(keywords.get(position));
    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

}
