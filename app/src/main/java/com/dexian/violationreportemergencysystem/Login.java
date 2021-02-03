package com.dexian.violationreportemergencysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
    Button btn_login, btn_register;
    String MAIN_LINK;// = "http://kitsware.com/projects/policeApp/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MAIN_LINK = getResources().getString(R.string.MAIN_LINK);

        ET_userName = findViewById(R.id.ET_userName);
        ET_pass = findViewById(R.id.ET_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = ET_userName.getText().toString();
                String pass = ET_pass.getText().toString();

                LoginRequest(user, pass);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDialog();
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

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    public static int getHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void RegisterDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(Login.this);
        dialog.setContentView(R.layout.deialog_register);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText ET_username = dialog.findViewById(R.id.ET_username);
        final EditText ET_pass = dialog.findViewById(R.id.ET_pass);
        final EditText ET_mobile = dialog.findViewById(R.id.ET_mobile);
        final EditText ET_email = dialog.findViewById(R.id.ET_email);
        final EditText ET_address = dialog.findViewById(R.id.ET_address);
        Button btn_register = dialog.findViewById(R.id.btn_register);

        // if button is clicked, close the custom dialog
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUserRequest(ET_username.getText().toString(), ET_pass.getText().toString(), ET_mobile.getText().toString(), ET_email.getText().toString(), ET_address.getText().toString());
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 90), ((getHeight(getApplicationContext()) / 100) * 50));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

    }
    public void RegisterUserRequest(String userName, String pass, String mobile, String email, String address){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "register.php?user="+userName+"&pass="+pass+"&mobile="+mobile+"&email="+email+"&address="+address;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("XIAN", ""+response);

                        if(response.trim().equals("OK")){
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "REGISTER SUCCESSFUL", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "REGISTER FAILED", Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.i("XIAN", ""+error);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "REGISTER FAILED "+error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
