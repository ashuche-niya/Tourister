package com.example.tourister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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
    String personEmail,personName,mobileno,userlocation="34 45";
    Button jointeam,createteam;
    String userenteredteamname="";
    //Retrofit retrofit;
    EditText teamname;
    grouplistadapter grouplistadapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

    @Override
    public void applyTexts(String teamname) {
        userenteredteamname=teamname;
    }
}