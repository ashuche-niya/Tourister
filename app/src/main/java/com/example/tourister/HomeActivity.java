package com.example.tourister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {

    GoogleSignInClient mGoogleSignInClient;
    List<groupmodel> groupmodellist=new ArrayList<>();
    Api apiintrerface;
    RecyclerView recyclerView;
    String personEmail,personName,mobileno,userlocation="";
    Button jointeam,createteam;
    String userenteredteamname="";
    //Retrofit retrofit;
    GoogleMap mgoogleMap;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    EditText teamname;
    grouplistadapter grouplistadapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        startLocationButtonClick();
        Intent intent=getIntent();
        mobileno= intent.getStringExtra("mobileno");
        recyclerView = findViewById(R.id.recyclerview);
        jointeam =findViewById(R.id.joingroup);
        createteam=findViewById(R.id.creategroup);
        teamname = findViewById(R.id.teamname);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
//        groupmodellist.add(new groupmodel("team1","INR348"));
//        groupmodellist.add(new groupmodel("team2","INR3"));
//        groupmodellist.add(new groupmodel("team3","INR34"));
        final grouplistadapter grouplistadapter=new grouplistadapter(groupmodellist,HomeActivity.this);
        recyclerView.setAdapter(grouplistadapter);
        grouplistadapter.setOnItemClickListener(new grouplistadapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<groupmodel> grouplist) {
                    Intent intent=new Intent(HomeActivity.this,containerActivity.class);
                    intent.putExtra("username",personName);
                    intent.putExtra("email",personEmail);
                    intent.putExtra("userlocation",userlocation);
                    intent.putExtra("mobileno",mobileno);
                    intent.putExtra("teamname",grouplist.get(position).getGroupname());
                    startActivity(intent);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(HomeActivity.this);
        if (acct != null) {
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl("https://tourister-app.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiintrerface=retrofit.create(Api.class);
        Call<String> call =apiintrerface.signInUser(new MainUser(personName,personEmail,mobileno));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(HomeActivity.this,"unable to add to server",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(HomeActivity.this,response.body(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"Faided to connect",Toast.LENGTH_SHORT).show();
            }
        });
//        openDialog();

//        Retrofit retrofit =new Retrofit.Builder()
//                .baseUrl("https://tourister-app.herokuapp.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        apiintrerface=retrofit.create(Api.class);
//        Call<String> call =apiintrerface.createteam(new Team(userenteredteamname,personName,userlocation,mobileno));
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (!response.isSuccessful())
//                {
//                    Toast.makeText(HomeActivity.this,"unable to add to server",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(HomeActivity.this,response.body(),Toast.LENGTH_SHORT).show();
//                    if (response.code()==200)
//                    {
//                        groupmodellist.add(new groupmodel(userenteredteamname));
//                        grouplistadapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//                Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });

        jointeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openDialog();
               //jointeamcall();
                userenteredteamname = teamname.getText().toString();
                Retrofit retrofit =new Retrofit.Builder()
                        .baseUrl("https://tourister-app.herokuapp.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api apiintrerface=retrofit.create(Api.class);
                Call<String> call =apiintrerface.jointeam(new Team(userenteredteamname,personName,userlocation,mobileno));
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful())
                        {
                            Toast.makeText(HomeActivity.this,"unable to add to server",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(HomeActivity.this,response.body(),Toast.LENGTH_SHORT).show();
                            if (response.body().equals("joined successfully"))
                            {
                                groupmodellist.add(new groupmodel(userenteredteamname));
                                grouplistadapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        createteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userenteredteamname = teamname.getText().toString();
                Retrofit retrofit =new Retrofit.Builder()
                        .baseUrl("https://tourister-app.herokuapp.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api apiintrerface=retrofit.create(Api.class);
                Call<String> call =apiintrerface.createteam(new Team(userenteredteamname,personName,userlocation,mobileno));
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful())
                        {
                            Toast.makeText(HomeActivity.this,"unable to add to server",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(HomeActivity.this,response.body(),Toast.LENGTH_SHORT).show();
                            if (response.body().equals("team created"))
                            {
                                groupmodellist.add(new groupmodel(userenteredteamname));
                                grouplistadapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout: {
                //Toast.makeText(this,"itemclicked",Toast.LENGTH_SHORT).show();
                signOut();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomeActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                userlocation=mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude();

            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
//            address = getAddress(getApplicationContext(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//
//            txtLocationResult.setText(address);
//
//            txtLocationResult.setAlpha(0);
//            txtLocationResult.animate().alpha(1).setDuration(300);
        }

    }
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {


                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {

                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";


                                Toast.makeText(HomeActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }


                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
                break;
        }

    }
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void applyTexts(String teamname) {
        userenteredteamname=teamname;
    }
}