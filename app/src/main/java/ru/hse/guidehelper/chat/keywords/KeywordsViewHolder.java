package ru.hse.guidehelper.chat.keywords;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class KeywordsViewHolder extends RecyclerView.ViewHolder {
    public final TextView keyword;

    KeywordsViewHolder(View view, int viewId) {
        super(view);
        keyword = view.findViewById(viewId);
    }
}
