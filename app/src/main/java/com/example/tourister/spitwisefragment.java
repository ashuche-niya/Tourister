package com.example.tourister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class spitwisefragment extends Fragment {
    View v;

    public spitwisefragment() {

    }

    group group;
    ArrayList<String> users;
    ArrayList<String> paid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.spitwisefragmentlayout,container,false);
//        Retrofit retrofit =new Retrofit.Builder()
//                .baseUrl("https://tourister-app.herokuapp.com")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Api apiintrerface=retrofit.create(Api.class);
//        Call<group> call =apiintrerface.teamname(containerActivity.teamname);
//        call.enqueue(new Callback<group>() {
//            @Override
//            public void onResponse(Call<group> call, Response<group> response) {
//                if (!response.isSuccessful())
//                {
//                    Toast.makeText(getContext(),"unable to add to server",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    //Toast.makeText(containerActivity.this,response.body(),Toast.LENGTH_SHORT).show();
//                    group=response.body();
//                    paid=group.getPaids();
//                    users=group.getUsernames();
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<group> call, Throwable t) {
//                Toast.makeText(getContext(),"Faided to connect",Toast.LENGTH_SHORT).show();
//            }
//        });


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef = database.getReference("user4");
        final EditText plain = v.findViewById(R.id.editText2);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference myRef = database.getReference("user4");
        final Button button = v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String amount = plain.getText().toString();
                TextView vi = v.findViewById(R.id.textView6);
                vi.setText(amount+"rupees");
                int n=5;
                int a[] = new int[n];
                a[0]  =Integer.parseInt(amount);
                a[1]= 200;
                a[2] = 300;
                a[3] =250;
                a[4]= 0;
                int total =0;
                int userId =0;
                for(int i=0;i<n;i++)
                {
                    total+=a[i];
                }
                double due[]= new double[n];
                for(int i=0;i<n;i++)
                {
                    due[i]=total/(double)n - (double)a[i];
                }


                //only positive
                String show="" ;
                if(due[userId]>0)
                    show =  settle(n,userId,due,userId);
                vi.setText(show);




//                myRef.setValue(amount);
//
//                FirebaseDatabase.getInstance().getReference().child("touramigo-878a1")
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    User user = snapshot.getValue(User.class);
//                                    System.out.println(user.email);
//                                }
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                            }
//                        });


            }
        });
        return v;
    }
    public String settle(int n,int index,double [] due,int userId)
    {
        String ret="";
        double min=1000000000;
        int min_index=0;
        for(int i=0;i<n;i++)
        {
            if((min>abs(due[index]+due[i])&&(due[i]<0)))
            {
                min=abs(due[index]+due[i]);
                min_index=i;
            }
        }
        min=due[index]+due[min_index];
        if(min<=0)
        {
            due[min_index]=min;
            // cout<<index+1<<" owes "<<min_index+1<<" INR "<<due[index]<<endl;
            if(index==userId)
                ret +="member"+index+1+" owes "+Integer.toString(min_index+1)+" INR "+Double.toString(due[index])+"\n";
            due[index]=0;
            return ret;
        }
        else
        {
            // cout<<index+1<<" owes "<<min_index+1<<" INR "<<abs(due[min_index])<<endl;
            ret +="member"+index+1+" owes "+Integer.toString(min_index+1)+" INR "+Double.toString(abs(due[min_index]))+"\n";
            due[min_index]=0;
            due[index]=min;
            ret = ret + settle(n,index,due,userId);
            return ret;
        }


    }
}