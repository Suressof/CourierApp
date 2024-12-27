package com.example.courierapp.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.courierapp.MainActivity;
import com.example.courierapp.R;

import java.util.Objects;

public class PasswordConfirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_confirm);

        Button btn = findViewById(R.id.button);

        EditText ed = findViewById(R.id.password);
        EditText ed1 = findViewById(R.id.passwordConfirm);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.equals(ed.getText().toString(), ed1.getText().toString())) {
                    Intent intent = new Intent(PasswordConfirm.this, AuthActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(PasswordConfirm.this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}