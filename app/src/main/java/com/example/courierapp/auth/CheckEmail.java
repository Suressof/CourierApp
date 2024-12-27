package com.example.courierapp.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.courierapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.Random;

public class CheckEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_email);

    }
    private boolean doubleClick = false;
    String code = "";
    String msg = "";
    public void SendEmail(View view) {
        EditText ed = findViewById(R.id.editCode);
        EditText editEmail = findViewById(R.id.editEmail);
        if(doubleClick){
            if(Objects.equals(code, ed.getText().toString())) {
                Toast.makeText(CheckEmail.this, "Код совпал", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CheckEmail.this, PasswordConfirm.class);

                startActivity(intent);
            }
            else {
                Toast.makeText(CheckEmail.this, "Код не совпал", Toast.LENGTH_LONG).show();
            }
        }
        else {
            EmailValidator validator = new EmailValidator();

            if (validator.validate(editEmail.getText().toString())) {
                doubleClick = true;
                // Почта действительна
                Toast.makeText(CheckEmail.this, "Код отправлен на почту", Toast.LENGTH_LONG).show();
                code = generateCode(4);
                msg = "<h1>CourierApp</h1> <h4>Ваш код подтверждения: " + code + "</h4>";
                //            LinearLayout constraintLayout = findViewById(R.id.code);
                //            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                //                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                //                    ConstraintLayout.LayoutParams.MATCH_PARENT);
                //            btn.setLayoutParams(layoutParams);
                //            constraintLayout.addView(btn);

                ed.setVisibility(View.VISIBLE);

                // Используйте класс EmailSender для отправки электронной почты
                EmailSender emailSender = new EmailSender("mathformul@gmail.com", "opyugpidyrpeyale", editEmail.getText().toString(), "Код для CourierApp", msg);
                emailSender.execute();
            } else {
                Snackbar snackbar = Snackbar.make(view, "Неверный формат почты", Snackbar.LENGTH_LONG);
                // Почта недействительна
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                snackbar.show();
            }
        }
    }

    private String generateCode(int length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

}