package com.thingsenz.energymeter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeControlActivity extends AppCompatActivity {

    SwitchButton powerModeBtn,lightCntrlBtn;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle saved){
        super.onCreate(saved);
        setContentView(R.layout.activity_home_control);
        powerModeBtn=findViewById(R.id.powerModeButton);
        lightCntrlBtn=findViewById(R.id.lightControlButton);
        String currPM="0",currLM="0";

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        powerModeBtn.setShadowEffect(true);
        powerModeBtn.setEnableEffect(true);

        lightCntrlBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                reference.child("S1").setValue((isChecked)?"1".toString():"0".toString());
            }
        });


        powerModeBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                reference.child("Mode").setValue((isChecked)?"1".toString():"0".toString());
            }
        });





    }

}
