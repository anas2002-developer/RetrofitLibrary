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

import com.example.retrofit.Activity.LoginActivity;
import com.example.retrofit.ModelResponse.RegisterResponse;
import com.example.retrofit.R;
import com.example.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText eName,eEmail,ePass;
    Button btnRegister;
    TextView txtLoginlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide actionBar
        getSupportActionBar().hide();

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        eName=findViewById(R.id.eName);
        eEmail=findViewById(R.id.eEmail);
        ePass=findViewById(R.id.ePass);
        btnRegister=findViewById(R.id.btnRegister);
        txtLoginlink=findViewById(R.id.txtLoginlink);

        btnRegister.setOnClickListener(this);
        txtLoginlink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRegister:
                userRegister();
                break;
            case R.id.txtLoginlink:
                switchtologin();
                break;
        }

    }

    private void userRegister() {

        String userName = eName.getText().toString();
        String userEmail = eEmail.getText().toString();
        String userPass = ePass.getText().toString();

        if (userName.isEmpty()){
            eName.requestFocus();
            eName.setError("Please enter your name");
            return;
        }
        if (userEmail.isEmpty()){
            eEmail.requestFocus();
            eEmail.setError("Please enter your email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            eEmail.requestFocus();
            eEmail.setError("Please enter correct email");
            return;
        }
        if (userPass.isEmpty()){
            ePass.requestFocus();
            ePass.setError("Please enter your password");
            return;
        }
        if (userPass.length()<8){
            ePass.requestFocus();
            ePass.setError("Please enter your password 8 dig");
            return;
        }

        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .register(userName,userEmail,userPass);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                RegisterResponse registerResponse=response.body();
                if (response.isSuccessful()){
                            Intent i =new Intent(MainActivity.this,LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                            Toast.makeText(MainActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void switchtologin() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}