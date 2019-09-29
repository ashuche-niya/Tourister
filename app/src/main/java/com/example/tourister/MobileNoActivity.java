package com.example.tourister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MobileNoActivity extends AppCompatActivity {

    EditText mobileno;
    String usermobileno;
    Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_no);
        mobileno = findViewById(R.id.mobileno);
        button = findViewById(R.id.addmobilenobtn);
        usermobileno = mobileno.getText().toString();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MobileNoActivity.this, HomeActivity.class);
                intent.putExtra("mobileno", usermobileno);
                startActivity(intent);
            }
        });
//        Intent intent =new Intent(MobileNoActivity.this,HomeActivity.class);
//        intent.putExtra("mobileno",usermobileno);
//        startActivity(intent);
    }
}
