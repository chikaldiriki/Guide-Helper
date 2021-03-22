package ru.hse.guidehelper.ui.bottomNavBar.subscriptions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubscriptionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SubscriptionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Subscriptions fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}