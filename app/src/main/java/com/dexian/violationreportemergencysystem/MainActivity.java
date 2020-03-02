package com.dexian.violationreportemergencysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_register, btn_login, btn_fir,  btn_addNewComplaint, btn_EmergencyButton, btn_getFirList, btn_GetComplaintList, btn_getEmergencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        btn_fir = findViewById(R.id.btn_fir);
        btn_addNewComplaint = findViewById(R.id.btn_addNewComplaint);
        btn_EmergencyButton = findViewById(R.id.btn_EmergencyButton);
        btn_getFirList = findViewById(R.id.btn_getFirList);
        btn_GetComplaintList = findViewById(R.id.btn_GetComplaintList);
        btn_getEmergencyList = findViewById(R.id.btn_getEmergencyList);

        final APIcalls apicalls = new APIcalls(getApplicationContext());

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_fir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_addNewComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_EmergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_getFirList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apicalls.GET_FIR_LIST("antor");
            }
        });
        btn_GetComplaintList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_getEmergencyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}
