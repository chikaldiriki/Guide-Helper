package ru.hse.guidehelper.chat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MyButtonObserver implements TextWatcher {

    private ImageButton mImageButton;

    public MyButtonObserver(ImageButton imageButton) {
        this.mImageButton = imageButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().trim().length() > 0) {
            mImageButton.setVisibility(View.VISIBLE);
        } else {
            mImageButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}