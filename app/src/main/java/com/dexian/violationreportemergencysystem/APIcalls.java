package com.dexian.violationreportemergencysystem;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIcalls {

    String TAG = "XIAN";
    Context context;
    String WEBSITE;

    private void searchPigeon(String searchText){
        String searchURL = "http://"+WEBSITE+"/Pigeons/search.php?s="+searchText;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Log.i(TAG,"Search URL : "+searchURL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                searchURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String jsonRes = response.toString();

                        Log.i(TAG,jsonRes);

                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();

                        JsonObject object = (JsonObject) parser.parse(jsonRes);// response will be the json String
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
                        Log.i(TAG,"REST API ERROR on Search:"+error.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
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
