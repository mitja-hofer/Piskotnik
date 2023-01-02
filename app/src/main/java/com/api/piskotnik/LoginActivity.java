package com.api.piskotnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText usernameInput;
    EditText passwordInput;
    String URLBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        URLBase = getString(R.string.URL_base);
        SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.remove("token");
        prefsEditor.apply();
    }

    public void login(View v) throws IOException {
        String username = String.valueOf(usernameInput.getText());
        String password = String.valueOf(passwordInput.getText());
        SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
        new Thread() {
            @Override
            public void run() {
                String tokenResponse = getToken(username, password);
                String token = "";
                try {
                    token = new JSONObject(tokenResponse).getString("token");

                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    prefsEditor.putString("token", token);
                    prefsEditor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        if (Objects.equals(prefs.getString("token", ""), "")) {
            Toast.makeText(getApplicationContext(),"Authentication error.",Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(LoginActivity.this, ListRecipesActivity.class);
            startActivity(intent);
        }
    }

    public String getToken(String username, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return HttpHelper.httpPostCall(URLBase+"/login", "", map);
    }

}