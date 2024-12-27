package com.example.courierapp.ui.completedorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompletedOrderViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CompletedOrderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}