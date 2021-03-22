package ru.hse.guidehelper.ui.bottomNavBar.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyOrdersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyOrdersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is My Orders fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}