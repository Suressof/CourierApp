package com.example.courierapp.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<String> mText = new MutableLiveData<>();
    public OrderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public void setData(String newData) {
        mText.setValue("1" + newData);
    }

    public LiveData<String> getText() {
        return mText;
    }

}