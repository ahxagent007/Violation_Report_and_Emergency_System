package com.dexian.violationreportemergencysystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button  btn_fir, btn_addNewComplaint, btn_EmergencyButton, btn_getFirList, btn_GetComplaintList, btn_getEmergencyList;
    String MAIN_LINK = "http://kitsware.com/projects/policeApp/";
    String USER_NAME = "ANTOR_THE_KING_ROBOT";

    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_fir = findViewById(R.id.btn_fir);
        btn_addNewComplaint = findViewById(R.id.btn_addNewComplaint);
        btn_EmergencyButton = findViewById(R.id.btn_EmergencyButton);
        btn_getFirList = findViewById(R.id.btn_getFirList);
        btn_GetComplaintList = findViewById(R.id.btn_GetComplaintList);
        btn_getEmergencyList = findViewById(R.id.btn_getEmergencyList);

        USER_NAME = getIntent().getStringExtra("USER_NAME");



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
                getLocation();
                EmergencyDialog();
            }
        });
        btn_getFirList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFIRListRequest(USER_NAME);
            }
        });
        btn_GetComplaintList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getComplaintListRequest(USER_NAME);
            }
        });
        btn_getEmergencyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmergencyListRequest(USER_NAME);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                askingLocationPermission();
                return;
            }
        }
        else{
            int permissionLocation = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionLocation != PackageManager.PERMISSION_GRANTED ){

                askingLocationPermission();
            }
        }



        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);

        Log.i("XIAN", "EMERGENCY LOC :: "+latitude+","+longitude);
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

    int PERMISSIONS_REQUEST_LOCATION = 101;
    // Location Permission
    private void askingLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
    }

    private void FIRDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_fir);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText ET_against = dialog.findViewById(R.id.ET_against);
        final EditText ET_details = dialog.findViewById(R.id.ET_details);
        Button btn_fir = dialog.findViewById(R.id.btn_fir);

        final Spinner SP_fir_type = dialog.findViewById(R.id.SP_fir_type);
        final EditText ET_against_address = dialog.findViewById(R.id.ET_against_address);

        // if button is clicked, close the custom dialog
        btn_fir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fir_type = SP_fir_type.getSelectedItem().toString();

                AddNewFIRRequest(USER_NAME, ET_against.getText().toString(), ET_details.getText().toString(), fir_type, ET_against_address.getText().toString());
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
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
        final EditText ET_subject = dialog.findViewById(R.id.ET_subject);

        // if button is clicked, close the custom dialog
        btn_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewComplaintRequest(USER_NAME, ET_place.getText().toString(), ET_details.getText().toString(), ET_subject.getText().toString());
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

    }
    private void EmergencyDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_emergency);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final EditText ET_message = dialog.findViewById(R.id.ET_message);
        Button btn_emergency = dialog.findViewById(R.id.btn_emergency);

        // if button is clicked, close the custom dialog
        btn_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmergencyButtonRequest(USER_NAME, +latitude+","+longitude, ET_message.getText().toString());
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

    }
    private void FIRListDialog(List<FIRList> firLists){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_fir_list);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final ListView LV_FIRList = dialog.findViewById(R.id.LV_FIRList);


        CustomAdapterForFIRList customAdapterForFIRlist = new CustomAdapterForFIRList(getApplicationContext(), firLists );
        LV_FIRList.setAdapter(customAdapterForFIRlist);

        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

    }
    private void ComplaintListDialog(List<ComplaintList> complaintLists){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_complaint_list);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final ListView LV_ComplaintList = dialog.findViewById(R.id.LV_ComplaintList);


        CustomAdapterForComplaintList customAdapterForComplaintList = new CustomAdapterForComplaintList(getApplicationContext(), complaintLists );
        LV_ComplaintList.setAdapter(customAdapterForComplaintList);

        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

    }
    private void EmergencyListDialog(List<EmergencyList> emergencyLists){

        // custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_emergency_list);
        //dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        final ListView LV_EmergencyList = dialog.findViewById(R.id.LV_EmergencyList);


        CustomAdapterForEmergencyList customAdapterForEmergencyList = new CustomAdapterForEmergencyList(getApplicationContext(), emergencyLists);
        LV_EmergencyList.setAdapter(customAdapterForEmergencyList);

        dialog.setCancelable(true);
        dialog.getWindow().setLayout(((getWidth(getApplicationContext()) / 100) * 100), ((getHeight(getApplicationContext()) / 100) * 100));
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

    }

    public void openMaps(String lati, String longi){

        Log.i("XIAN", "lati : "+lati+ " longi : "+longi);
        String uri = String.format(Locale.ENGLISH, "geo:%s,%s", lati, longi); //google.com/maps?q= //geo:
        Log.i("XIAN", uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);

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

    public void AddNewFIRRequest(String userName, String against, String details, String type, String address){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "fir.php?user="+userName+"&against="+against+"&details="+details+"&type="+type+"&address="+address;

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
    public void AddNewComplaintRequest(String userName, String place, String details, String subject){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "complaint.php?user="+userName+"&place="+place+"&details="+details+"&subject="+subject;

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
    public void EmergencyButtonRequest(String userName, String loc, String msg){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "emergency.php?user="+userName+"&location="+loc+"&message="+msg;

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
    public void getFIRListRequest(String userName){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "fir.php?user="+userName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("XIAN", ""+response);

                        List<FIRList> firLists = new ArrayList<FIRList>();

                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(response);
                            Gson gson = new Gson();

                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                for (int i=0;i<len;i++){
                                    firLists.add(gson.fromJson(jsonArray.get(i).toString(), FIRList.class));
                                }
                            }

                            Log.i("XIAN","firLists Size : "+firLists.size());

                            FIRListDialog(firLists);

                        } catch (final JSONException e) {
                            Log.i("XIAN", ""+e);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "getFIRListRequest FAILED "+e, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "getFIRListRequest FAILED "+error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void getComplaintListRequest(String userName){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "complaint.php?user="+userName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("XIAN", ""+response);

                        List<ComplaintList> complaintLists = new ArrayList<ComplaintList>();

                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(response);
                            Gson gson = new Gson();

                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                for (int i=0;i<len;i++){
                                    complaintLists.add(gson.fromJson(jsonArray.get(i).toString(), ComplaintList.class));
                                }
                            }

                            Log.i("XIAN","complaintLists Size : "+complaintLists.size());

                            ComplaintListDialog(complaintLists);

                        } catch (final JSONException e) {
                            Log.i("XIAN", ""+e);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "getComplaintListRequest FAILED "+e, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "getComplaintListRequest FAILED "+error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void getEmergencyListRequest(String userName){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = MAIN_LINK + "emergency.php?user="+userName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("XIAN", ""+response);

                        List<EmergencyList> emergencyLists = new ArrayList<EmergencyList>();

                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(response);
                            Gson gson = new Gson();

                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                for (int i=0;i<len;i++){
                                    emergencyLists.add(gson.fromJson(jsonArray.get(i).toString(), EmergencyList.class));
                                }
                            }

                            Log.i("XIAN","emergencyLists Size : "+emergencyLists.size());
                            EmergencyListDialog(emergencyLists);


                        } catch (final JSONException e) {
                            Log.i("XIAN", ""+e);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "getEmergencyListRequest FAILED "+e, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "getEmergencyListRequest FAILED "+error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public class CustomAdapterForFIRList extends BaseAdapter {
        private Context context;
        private List<FIRList> firLists;

        private TextView TV_id, TV_user, TV_against, TV_details, TV_action;

        public CustomAdapterForFIRList(Context context, List<FIRList> firLists) {
            this.context = context;
            this.firLists = firLists;
        }
        @Override
        public int getCount() {
            return firLists.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = LayoutInflater.from(context).inflate(R.layout.single_fir, parent, false);


            //add data to UI
            TV_id = view.findViewById(R.id.TV_id);
            TV_user = view.findViewById(R.id.TV_user);
            TV_against = view.findViewById(R.id.TV_against);
            TV_details = view.findViewById(R.id.TV_details);
            TV_action = view.findViewById(R.id.TV_action);

            TV_id.setText(firLists.get(position).getId());
            TV_user.setText(firLists.get(position).getUser());
            TV_against.setText(firLists.get(position).getAgainst());
            TV_details.setText(firLists.get(position).getDetails());
            TV_action.setText(firLists.get(position).getAction());


            return view;
        }
    }
    public class CustomAdapterForComplaintList extends BaseAdapter {
        private Context context;
        private List<ComplaintList> complaintLists;

        private TextView TV_id, TV_user, TV_place, TV_details, TV_action;

        public CustomAdapterForComplaintList(Context context, List<ComplaintList> complaintLists) {
            this.context = context;
            this.complaintLists = complaintLists;
        }
        @Override
        public int getCount() {
            return complaintLists.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = LayoutInflater.from(context).inflate(R.layout.single_complaint, parent, false);


            //add data to UI
            TV_id = view.findViewById(R.id.TV_id);
            TV_user = view.findViewById(R.id.TV_user);
            TV_place = view.findViewById(R.id.TV_place);
            TV_details = view.findViewById(R.id.TV_details);
            TV_action = view.findViewById(R.id.TV_action);

            TV_id.setText(complaintLists.get(position).getId());
            TV_user.setText(complaintLists.get(position).getUser());
            TV_place.setText(complaintLists.get(position).getPlace());
            TV_details.setText(complaintLists.get(position).getDetails());
            TV_action.setText(complaintLists.get(position).getAction());


            return view;
        }
    }
    public class CustomAdapterForEmergencyList extends BaseAdapter {
        private Context context;
        private List<EmergencyList> emergencyLists;

        private TextView TV_id, TV_user, TV_action;
        private Button btn_location;

        public CustomAdapterForEmergencyList(Context context, List<EmergencyList> emergencyLists) {
            this.context = context;
            this.emergencyLists = emergencyLists;
        }
        @Override
        public int getCount() {
            return emergencyLists.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = LayoutInflater.from(context).inflate(R.layout.single_emergency, parent, false);


            //add data to UI
            TV_id = view.findViewById(R.id.TV_id);
            TV_user = view.findViewById(R.id.TV_user);
            TV_action = view.findViewById(R.id.TV_action);
            btn_location = view.findViewById(R.id.btn_location);

            TV_id.setText(emergencyLists.get(position).getId());
            TV_user.setText(emergencyLists.get(position).getUser());
            TV_action.setText(emergencyLists.get(position).getAction());
            btn_location.setText(emergencyLists.get(position).getLocation());

            btn_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String loc[] = emergencyLists.get(position).getLocation().split(",");
                    openMaps(loc[0], loc[1]);
                }
            });


            return view;
        }
    }

    /*
        adapter = new CustomAdapter(getApplicationContext(), infoData.getItemList());
        LV_itemAvailable.setAdapter(adapter);

     */

    /*

    Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    JsonObject object = (JsonObject) parser.parse(jsonRes);// response will be the json String
    PigeonList pList = gson.fromJson(object, PigeonList.class);

    */
}
