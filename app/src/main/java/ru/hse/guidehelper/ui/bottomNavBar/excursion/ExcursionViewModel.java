package ru.hse.guidehelper.ui.bottomNavBar.excursion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExcursionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExcursionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is excursion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}