package com.example.instagram;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

public class DBHelper {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    protected boolean validateEmail(String textInputEmail){
        String emailInput = textInputEmail.trim();

        if (emailInput.isEmpty()){
//            editText.setError("Field can't be empty");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
//            editText.setError("Enter a valid email address.");
            return false;
        }else {
//            editText.setError(null);
            return true;
        }
    }

    protected boolean validateUsername(String textInputUsername) {
        String usernameInput = textInputUsername.trim();

        if (usernameInput.isEmpty()) {
            return false;
        } else if (usernameInput.length() > 15 || usernameInput.length() < 5) {
            //editText.setError("Username too long");
            return false;
        } else {
            // editText.setError(null);
            return true;
        }
    }

    protected boolean validatePassword(String textInputPassword) {
        String passwordInput = textInputPassword.trim();

        if (passwordInput.isEmpty()) {
//            editText.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            editText.setError("Password too weak");
            return false;
        } else {
//            editText.setError(null);
            return true;
        }
    }

}
