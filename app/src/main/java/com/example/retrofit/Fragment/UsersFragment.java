package com.example.retrofit.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.retrofit.ModelResponse.FetchUsersResponse;
import com.example.retrofit.ModelResponse.User;
import com.example.retrofit.R;
import com.example.retrofit.RetrofitClient;
import com.example.retrofit.UserAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment {

    RecyclerView vRV;
    List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vRV=view.findViewById(R.id.vRV);
        vRV.setHasFixedSize(true);
        vRV.setLayoutManager(new LinearLayoutManager(getActivity()));


        Call<FetchUsersResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .fetchusers();

        call.enqueue(new Callback<FetchUsersResponse>() {
            @Override
            public void onResponse(Call<FetchUsersResponse> call, Response<FetchUsersResponse> response) {

                //                FetchUsersResponse fetchUsersResponse = response.body();
                if (response.isSuccessful()){
                    userList = response.body().getUserList();
                    vRV.setAdapter(new UserAdapter(getActivity(),userList));
                }
                else {
                    Toast.makeText(getActivity(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchUsersResponse> call, Throwable t) {

            }
        });
    }
}