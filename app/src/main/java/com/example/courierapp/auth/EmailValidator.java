package com.example.courierapp.auth;

import javax.mail.internet.InternetAddress;

public class EmailValidator {
    public boolean validate(String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
