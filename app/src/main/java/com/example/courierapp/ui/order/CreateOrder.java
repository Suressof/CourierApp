package com.example.courierapp.ui.order;

public class CreateOrder {
    int id;
    String creationDate;
    String customerPhone;
    int paymentWayId;
    boolean isFinished;

    public void setId(int id) {
        this.id = id;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    public void setcustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    public void setPaymentWayId(int paymentWayId) {
        this.paymentWayId = paymentWayId;
    }
    public void setisFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
