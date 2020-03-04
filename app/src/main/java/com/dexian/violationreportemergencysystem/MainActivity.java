package com.dexian.violationreportemergencysystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
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

public class MainActivity extends AppCompatActivity {

    Button btn_register, btn_fir, btn_addNewComplaint, btn_EmergencyButton, btn_getFirList, btn_GetComplaintList, btn_getEmergencyList;
    String MAIN_LINK = "http://kitsware.com/projects/policeApp/";
    String USER_NAME = "ANTOR_THE_KING_ROBOT";

    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_register = findViewById(R.id.btn_register);
        btn_fir = findViewById(R.id.btn_fir);
        btn_addNewComplaint = findViewById(R.id.btn_addNewComplaint);
        btn_EmergencyButton = findViewById(R.id.btn_EmergencyButton);
        btn_getFirList = findViewById(R.id.btn_getFirList);
        btn_GetComplaintList = findViewById(R.id.btn_GetComplaintList);
        btn_getEmergencyList = findViewById(R.id.btn_getEmergencyList);

        final APIcalls apicalls = new APIcalls(getApplicationContext());

        USER_NAME = getIntent().getStringExtra("USER_NAME");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDialog();
            }
        });

        btn_fir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FIRDialog();
            }
        });
        btn_addNewComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComplaintDialog();
            }
        });
        btn_EmergencyButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {


                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }


                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

                Log.i("XIAN", "EMERGENCY LOC :: "+longitude+","+latitude);

                EmergencyButtonRequest(USER_NAME, longitude+","+latitude);
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
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void RegisterDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
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

    private void FIRDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.deialog_register);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText ET_against = dialog.findViewById(R.id.ET_against);
        final EditText ET_details = dialog.findViewById(R.id.ET_details);
        Button btn_fir = dialog.findViewById(R.id.btn_fir);

        // if button is clicked, close the custom dialog
        btn_fir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewFIRRequest(USER_NAME, ET_against.getText().toString(), ET_details.getText().toString());
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 90), ((getHeight(getApplicationContext()) / 100) * 50));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

    }

    private void ComplaintDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_complaint);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText ET_place = dialog.findViewById(R.id.ET_place);
        final EditText ET_details = dialog.findViewById(R.id.ET_details);
        Button btn_complaint = dialog.findViewById(R.id.btn_complaint);

        // if button is clicked, close the custom dialog
        btn_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewComplaintRequest(USER_NAME, ET_place.getText().toString(), ET_details.getText().toString());
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 90), ((getHeight(getApplicationContext()) / 100) * 50));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

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

    public void AddNewFIRRequest(String userName, String against, String details){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "fir.php?user="+userName+"&against="+against+"&details="+details;

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
                                    Toast.makeText(getApplicationContext(), "FIR SUCCESSFUL", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "FIR FAILED", Toast.LENGTH_LONG).show();
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

    public void AddNewComplaintRequest(String userName, String place, String details){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "complaint.php?user="+userName+"&place="+place+"&details="+details;

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
                                    Toast.makeText(getApplicationContext(), "COMPLAINT SUCCESSFUL", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "COMPLAINT FAILED", Toast.LENGTH_LONG).show();
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

    public void EmergencyButtonRequest(String userName, String loc){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "emergency.php?user="+userName+"&location="+loc;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("XIAN", ""+response);

                        if(response.trim().equals("ADDED")){
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "EMERGENCY SUCCESSFUL", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "EMERGENCY FAILED", Toast.LENGTH_LONG).show();
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
