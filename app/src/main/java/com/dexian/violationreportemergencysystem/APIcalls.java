package com.dexian.violationreportemergencysystem;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIcalls {

    String TAG = "XIAN";
    Context context;
    String WEBSITE;

    String MAIN_LINK = "http://kitsware.com/projects/policeApp/";

    String REGISTER = MAIN_LINK + "register.php?user=<STRING>&pass=<STRING>&mobile=<STRING>&email=<STRING>&address=<STRING>"; //RESPONSE: OK or ERROR!
    String LOGIN = "login.php?user=<STRING>&pass=<STRING>"; //RESPONSE: OK or ERROR!
    String ADD_NEW_FIR = "fir.php?user=<STRING>&against=<STRING>&details=<STRING(SPACE MUST BE REPLACED BY UNDERSCORE)>"; //RESPONSE: OK or ERROR!
    String ADD_NEW_COMPLAINT = "complaint.php?user=<STRING>&place=<STRING>&details=<STRING(SPACE MUST BE REPLACED BY UNDERSCORE)>"; //RESPONSE: OK or ERROR!
    String EMERGENCY_BUTTON = "emergency.php?user=<STRING>&location=<LATITUDE,LONGITUDE>"; //RESPONSE: OK or ERROR!

    String GET_FIR_LIST = "fir.php?user=<STRING>"; //JSON Format
    String GET_COMPLAINT_LIST = "complaint.php?user=<STRING>"; //JSON Format
    String GET_EMERGENCY_LIST = "emergency.php?user=<STRING>"; //JSON Format

    public APIcalls(Context context) {
        this.context = context;
    }

    public void GET_FIR_LIST(String user){
        String URL = MAIN_LINK + "fir.php?user="+user; //JSON Format

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Log.i(TAG,"GET_FIR_LIST URL : "+URL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String jsonRes = response.toString();

                        /*Log.i(TAG,jsonRes);

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();

                        JsonArray object = (JsonArray) parser.parse(jsonRes);*/// response will be the json String

                        Log.i(TAG, "GET_FIR_LIST JSON RESPONSE : "+jsonRes);
                        //PigeonList emp = gson.fromJson(object, PigeonList.class);

                        //Log.i(TAG,emp.getRecords().toString());

                        //List<Pigeon> pp = emp.getRecords();

                        //PigeonList = pp;
                        //setAdapter();
                        //PB_loading.setVisibility(View.INVISIBLE);

                        //Log.i(TAG,"pp size : "+pp.size());
                        //Log.i(TAG,"pp 0 name = "+pp.get(0).getPigeonRingNumber());



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"ERROR on GET_FIR_LIST : "+error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public String LOG_IN(String user, String pass){
        String URL = MAIN_LINK + "login.php?user="+user+"&pass="+pass; //JSON Format

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Log.i(TAG,"LOG_IN URL : "+URL);

        final String[] temp = {"NULL"};

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "LOG_IN RESPONSE : "+response);
                        String jsonRes = response.toString();
                        Log.i(TAG, "LOG_IN j RESPONSE : "+jsonRes);
                        temp[0] = jsonRes;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"ERROR on LOG_IN : "+error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

        return temp[0];
    }

    public String jsonArrayRequest(){

        RequestQueue queue = Volley.newRequestQueue(context);

        String URL = MAIN_LINK + "login.php?user="+"xian"+"&pass="+"xian"; //JSON Format

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.i("XIAN", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("XIAN", error.toString());
                    }
                }
        );



        // add it to the RequestQueue
        queue.add(getRequest);
        return "XIANxxxxxxxx";
    }

    public void StringRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String URL = MAIN_LINK + "login.php?user="+"xian"+"&pass="+"xian"; //JSON Format

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("XIAN", ""+response);


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

    private void createProduct(String name, String description, String price, String category_id, String catergory_name){
        String createURL = "http://"+WEBSITE+"/api/product/create.php";

        String jsonString = "{\n" +
                "    \"name\" : \"Amazing Pillow 2.0\",\n" +
                "    \"price\" : \"199\",\n" +
                "    \"description\" : \"The best pillow for amazing programmers.\",\n" +
                "    \"category_id\" : 2,\n" +
                "    \"created\" : \"2018-06-01 00:35:07\"\n" +
                "}";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("name",name);
        jsonParams.put("description",description);
        jsonParams.put("price",price);
        jsonParams.put("category_id",category_id);
        jsonParams.put("catergory_name",catergory_name);

        Log.i(TAG, "JSON conversion = "+ new JSONObject(jsonParams));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                createURL,
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = (String)response.get("message");
                            Log.i(TAG,"Message on Create product : "+msg);
                        } catch (JSONException e) {
                            Log.i(TAG, "Message exception "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG,"REST ERROR :"+error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
