package com.example.retrofit.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.retrofit.R;
import com.example.retrofit.SharedPrefManager;


public class DashboardFragment extends Fragment {

    TextView txtUsername,txtEmail;
    SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);

        //getActivity because we are in fragment not activity otherwise it will be getApplicationContext()
        sharedPrefManager = new SharedPrefManager(getActivity());

        //to change text in dashboard fragment
        txtUsername.setText("Hey! "+sharedPrefManager.getUser().getUsername());
        txtEmail.setText(sharedPrefManager.getUser().getEmail());
        return view;
    }
}