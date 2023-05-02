package com.example.retrofit.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.retrofit.Fragment.DashboardFragment;
import com.example.retrofit.Fragment.ProfileFragment;
import com.example.retrofit.Fragment.UsersFragment;
import com.example.retrofit.ModelResponse.DeleteAccountResponse;
import com.example.retrofit.R;
import com.example.retrofit.RetrofitClient;
import com.example.retrofit.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView vBNV;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        vBNV=findViewById(R.id.vBNV);

        vBNV.setOnNavigationItemSelectedListener(this);
        loadFrag(new DashboardFragment());

        //for deleting and logging out account
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.item_dashboard:
                frag = new DashboardFragment();
                break;
            case R.id.item_users:
                frag = new UsersFragment();
                break;
            case R.id.item_profile:
                frag = new ProfileFragment();
                break;
        }

        if (frag!=null){
            loadFrag(frag);
        }

        return true;
    }

    private void loadFrag(Fragment frag){

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.RL_home,frag)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_logout:
                logout_acc();
                break;
            case R.id.item_deleteacc:
                delete_acc();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void delete_acc() {
        Call<DeleteAccountResponse> call = RetrofitClient.getInstance()
                .getApi()
                .deleteaccount(sharedPrefManager.getUser().getId());

        call.enqueue(new Callback<DeleteAccountResponse>() {
            @Override
            public void onResponse(Call<DeleteAccountResponse> call, Response<DeleteAccountResponse> response) {
                DeleteAccountResponse deleteAccountResponse=response.body();

                if (response.isSuccessful()){
                    if (deleteAccountResponse.getError().equals("200")){
                        logout_acc();
                        Toast.makeText(HomeActivity.this, deleteAccountResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(HomeActivity.this, deleteAccountResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(HomeActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteAccountResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout_acc() {
        sharedPrefManager.loggout();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }
}