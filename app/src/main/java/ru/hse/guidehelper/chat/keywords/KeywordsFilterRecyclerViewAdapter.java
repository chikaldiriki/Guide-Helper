package ru.hse.guidehelper.chat.keywords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import ru.hse.guidehelper.R;
import ru.hse.guidehelper.api.RequestHelper;

public class KeywordsFilterRecyclerViewAdapter extends RecyclerView.Adapter<KeywordsViewHolder> {

    private List<String> popularKeywords;
    private final Function<String, View.OnClickListener> keywordOnClickListener;

    public KeywordsFilterRecyclerViewAdapter(String userMail, Function<String, View.OnClickListener> listener) {
        popularKeywords = RequestHelper.getPopularKeywordsFromDB(userMail);
        keywordOnClickListener = listener;
        if (popularKeywords == null) {
            popularKeywords = Collections.singletonList("Something went wrong...");
        }
        if (popularKeywords.isEmpty()) {
            popularKeywords = Collections.singletonList("No keywords");
        }
    }

    @NonNull
    @NotNull
    @Override
    public KeywordsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keyword_filter_item, parent, false);
        return new KeywordsViewHolder(view, R.id.id_keyword_filter);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull KeywordsViewHolder holder, int position) {
        holder.keyword.setText(popularKeywords.get(position));
        holder.itemView.setTag(popularKeywords.get(position));
        holder.itemView.setOnClickListener(keywordOnClickListener.apply(popularKeywords.get(position)));
    }

    @Override
    public int getItemCount() {
        return popularKeywords.size();
    }

    public void updatePopularKeywords(String userMail) {
        popularKeywords = RequestHelper.getNewPopularKeywords(userMail);
    }
}
