package com.example.tourister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

//import android.support.design.widget.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
//import com.google.android.support.design.widget.TabLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class containerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public TabLayout tabLayout;
    public Toolbar toolbar;
    public Viewpageadapter adapter;
    static String username,mobileno,email,teamname,location;
    group group;
    public ViewPager viewPager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Intent intent=getIntent();
        location=intent.getStringExtra("userlocation");
//        toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView =findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadfragment(new locationfragment());
        teamname=intent.getStringExtra("teamname");
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://tourister-app.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api apiintrerface=retrofit.create(Api.class);
        Call<group> call =apiintrerface.teamname(teamname);
        call.enqueue(new Callback<group>() {
            @Override
            public void onResponse(Call<group> call, Response<group> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(containerActivity.this,"unable to add to server",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(containerActivity.this,response.body(),Toast.LENGTH_SHORT).show();
                    group=response.body();
                }
            }

            @Override
            public void onFailure(Call<group> call, Throwable t) {
                Toast.makeText(containerActivity.this,"Faided to connect",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.location:
                loadfragment(new locationfragment());
                break;
            case R.id.album:
                loadfragment(new albumfragment());
                break;
            case R.id.chat:
                loadfragment(new chatfragment());
                break;
            case R.id.spitwise:
                loadfragment(new spitwisefragment());
                break;
        }
        return false;
    }
    public  boolean loadfragment(Fragment fragment)
    {
        if(fragment!=null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentcontainer,fragment)
                    .commit();
            return true;
        }
        return false;
    }
}

