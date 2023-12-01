package com.example.eventmanager.model;

public class ValidationModel {

    public boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
    }

    public boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}
