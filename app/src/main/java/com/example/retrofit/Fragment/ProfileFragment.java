package com.example.retrofit.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrofit.Activity.LoginActivity;
import com.example.retrofit.ModelResponse.LoginResponse;
import com.example.retrofit.ModelResponse.UpdatePassResponse;
import com.example.retrofit.R;
import com.example.retrofit.RetrofitClient;
import com.example.retrofit.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    EditText eUsername, eEmail, eCurrpass,eNewpass;
    Button btnUpdate1,btnUpdate2;
    int id;
    String email;
    SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //for updating username and email on basis of sp id
        eUsername=view.findViewById(R.id.eUsername_profile);
        eEmail=view.findViewById(R.id.eEmail_profile);
        btnUpdate1=view.findViewById(R.id.btnUpdate1);

        sharedPrefManager = new SharedPrefManager(getActivity());
        id = sharedPrefManager.getUser().getId();
        btnUpdate1.setOnClickListener(this);


        //for updating password on basis of sp email
        eCurrpass = view.findViewById(R.id.eCurrpass_profile);
        eNewpass = view.findViewById(R.id.eNewpass_profile);
        btnUpdate2=view.findViewById(R.id.btnUpdate2);

        email = sharedPrefManager.getUser().getEmail();
        btnUpdate2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnUpdate1:
                update3();
                Toast.makeText(getActivity(), "update1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnUpdate2:
                update2();
//                Toast.makeText(getActivity(), "update2", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void update3() {
        String username = eUsername.getText().toString().trim();
        String email = eEmail.getText().toString().trim();

        if (username.isEmpty()) {
            eUsername.requestFocus();
            eUsername.setError("Empty Username");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eEmail.requestFocus();
            eEmail.setError("Invalid Email");
        }

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateuser(username, email, id);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse updateResponse = response.body();
                if (response.isSuccessful()) {
                    if (updateResponse.getError().equals("200")) {

                        sharedPrefManager.saveUser(updateResponse.getUser());
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //for updating username and email
    private void update2() {
        String currentpass = eCurrpass.getText().toString().trim();
        String newpass = eNewpass.getText().toString().trim();

        if (currentpass.isEmpty()){
            eCurrpass.requestFocus();
            eCurrpass.setError("Empty Pass");
        }
        if (newpass.isEmpty()){
            eNewpass.requestFocus();
            eNewpass.setError("Empty Pass");
        }
        if (currentpass.length()<8){
            eCurrpass.requestFocus();
            eCurrpass.setError("8 digit Pass");
        }
        if (newpass.length()<8){
            eNewpass.requestFocus();
            eNewpass.setError("8 digit Pass");
        }

        //for updating password
        Call<UpdatePassResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updatepassword(currentpass,email,newpass);

        call.enqueue(new Callback<UpdatePassResponse>() {
            @Override
            public void onResponse(Call<UpdatePassResponse> call, Response<UpdatePassResponse> response) {
                UpdatePassResponse updatePassResponse = response.body();

                if (response.isSuccessful()){
                    if (updatePassResponse.getError().equals("200")){
                        Toast.makeText(getActivity(), updatePassResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(),updatePassResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdatePassResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}