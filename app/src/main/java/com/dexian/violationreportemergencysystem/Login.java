package com.dexian.violationreportemergencysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Login extends AppCompatActivity {

    EditText ET_userName, ET_pass;
    Button btn_login;

    String MAIN_LINK = "http://kitsware.com/projects/policeApp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ET_userName = findViewById(R.id.ET_userName);
        ET_pass = findViewById(R.id.ET_pass);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = ET_userName.getText().toString();
                String pass = ET_pass.getText().toString();

                LoginRequest(user, pass);
            }
        });
    }

    public void LoginRequest(final String user, String pass){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "login.php?user="+user+"&pass="+pass; //JSON Format

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("XIAN", ""+response.trim()+" "+response.trim().length());

                        if(response.trim().equals("OK")){
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("USER_NAME", user);
                            startActivity(i);
                            finish();
                        }else{
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "SORRY WRONG INFO", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("XIAN", ""+error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
