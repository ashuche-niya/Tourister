package com.example.tourister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class locationfragment extends Fragment implements OnMapReadyCallback {
    View v;

    public locationfragment() {

    }
    group group;
    GoogleMap mgoogleMap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.locationframentlayout,container,false);
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://tourister-app.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api apiintrerface=retrofit.create(Api.class);
        Call<group> call =apiintrerface.teamname(containerActivity.teamname);
        call.enqueue(new Callback<group>() {
            @Override
            public void onResponse(Call<group> call, Response<group> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(getContext(),"unable to add to server",Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(containerActivity.this,response.body(),Toast.LENGTH_SHORT).show();
                    group=response.body();

                }
            }

            @Override
            public void onFailure(Call<group> call, Throwable t) {
                Toast.makeText(getContext(),"Faided to connect",Toast.LENGTH_SHORT).show();
            }
        });



        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Marker m1 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(38.609556, -1.139637))
                .anchor(0.5f, 0.5f)
                .title("Title1")
                .snippet("Snippet1"));


        Marker m2 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.4272414,-3.7020037))
                .anchor(0.5f, 0.5f)
                .title("Title2")
                .snippet("Snippet2"));

        Marker m3 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(43.2568193,-2.9225534))
                .anchor(0.5f, 0.5f)
                .title("Title3")
                .snippet("Snippet3"));
    }
}