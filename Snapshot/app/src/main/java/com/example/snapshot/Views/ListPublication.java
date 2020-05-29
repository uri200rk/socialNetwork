package com.example.snapshot.Views;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.Adapter.PublicationAdapter;
import com.example.snapshot.Clases.Publication;
import com.example.snapshot.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListPublication extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    //declarations of elements

    RecyclerView recyclerPublications;
    ArrayList<Publication> listPublications;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public static ListPublication newInstance(){
        return new ListPublication();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list_publication, container, false);

        listPublications = new ArrayList<>();

        recyclerPublications = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerPublications.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerPublications.setHasFixedSize(true);

        request = Volley.newRequestQueue(getContext());

        loadWebService();

        return view;
    }

    private void loadWebService() {

        String url = "http://uri200rk.alwaysdata.net/webService/consulta_publicaciones.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(), "error al conectar" + error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {

        JSONArray json = response.optJSONArray("publication");

        try {

            for (int i = 0; i<json.length(); i++){


                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);


                listPublications.add(new Publication (jsonObject.optInt("idPublication") , jsonObject.optInt("idUser") , jsonObject.optString("nick") ,jsonObject.optInt("likes") , jsonObject.optString("title"),
                                                    jsonObject.optString("description") , jsonObject.optString("idMedia")  , jsonObject.optString("date")));

            }

            PublicationAdapter adapter = new PublicationAdapter(listPublications , getContext());
            recyclerPublications.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexion con el servidor" + response, Toast.LENGTH_LONG).show();
        }

    }



}
