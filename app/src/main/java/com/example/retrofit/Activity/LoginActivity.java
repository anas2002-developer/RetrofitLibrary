package com.example.retrofit.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.ModelResponse.LoginResponse;
import com.example.retrofit.ModelResponse.RegisterResponse;
import com.example.retrofit.R;
import com.example.retrofit.RetrofitClient;
import com.example.retrofit.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    EditText eEmail,ePass;
    Button btnLogin;
    TextView txtRegisterlink;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hide actionBar
        getSupportActionBar().hide();

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        eEmail=findViewById(R.id.eEmail);
        ePass=findViewById(R.id.ePass);
        btnLogin=findViewById(R.id.btnLogin);
        txtRegisterlink=findViewById(R.id.txtRegisterlink);

        btnLogin.setOnClickListener(this);
        txtRegisterlink.setOnClickListener(this);

        sharedPrefManager = new SharedPrefManager(getApplicationContext());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.txtRegisterlink:
                switchtoRegister();
                break;
        }

    }

    private void userLogin() {

        String userEmail = eEmail.getText().toString();
        String userPass = ePass.getText().toString();

        if (userEmail.isEmpty()){
            eEmail.requestFocus();
            eEmail.setError("Enter email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            eEmail.requestFocus();
            eEmail.setError("Enter correct email format");
            return;
        }
        if (userPass.isEmpty()){
            ePass.requestFocus();
            ePass.setError("Enter your password");
            return;
        }
        if (userPass.length()<8){
            ePass.requestFocus();
            ePass.setError("Enter password (8-char)");
            return;
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .login(userEmail,userPass);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse loginResponse = response.body();

                //successful api call
                if (response.isSuccessful()){

                    if (loginResponse.getError().equals("200")) {
                        //login successful

                        //for saving user details in sharedPreferences
                        sharedPrefManager.saveUser(loginResponse.getUser());

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //user doest exist
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            //unsuccessfull api call
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void switchtoRegister() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPrefManager.isLoggedIn()){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}