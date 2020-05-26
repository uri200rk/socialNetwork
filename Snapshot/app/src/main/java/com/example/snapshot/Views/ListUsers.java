package com.example.snapshot.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.snapshot.Adapter.UsersAdapter;
import com.example.snapshot.Clases.User;
import com.example.snapshot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListUsers extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    //declarations of elements

    RecyclerView recyclerUsers;
    ArrayList<User> listUsers;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;



    //configurations with inflate fragment

    public static ListUsers newInstance(){
        return new ListUsers();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list_users, container, false);

        listUsers = new ArrayList<>();

        recyclerUsers = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerUsers.setHasFixedSize(true);

        request = Volley.newRequestQueue(getContext());




        loadWebService();

        return view;
    }

    private void loadWebService() {

        String url = "http://192.168.1.16/webService/consultar_lista_usuario.php";
        Log.i("e", url);

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

        //get user logged
        MainActivity mainActivity = new MainActivity();
        final User userLogged = mainActivity.getUser();
        //fin get user logged

        User user = null;

        JSONArray json = response.optJSONArray("user");
        try {

            for (int i = 0; i<json.length(); i++){

                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                if (!jsonObject.optString("idUser").equals(Integer.toString(userLogged.getIdUser()))){
                    Log.i("epa", Integer.toString(userLogged.getIdUser()) + "-------------------------------------------------------");
                    listUsers.add(new User (jsonObject.optString("nick")));

                }




            }

            UsersAdapter adapter = new UsersAdapter((listUsers));

            adapter.setOnclickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {


                    AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                    alerta.setMessage("Deseas seguir a " + listUsers.get(recyclerUsers.getChildAdapterPosition(v)).getNick() + "?")
                            .setCancelable(false)
                            .setPositiveButton("si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            "Acceptado:" ,
                                            Toast.LENGTH_SHORT).show();
                                    String userFollowing = listUsers.get(recyclerUsers.getChildAdapterPosition(v)).getNick();
                                    String url = "http://192.168.1.16/webService/insertar_follow.php";
                                    ejecutarSerivcio(url, userFollowing, userLogged);


                                }
                            })
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog titulo = alerta.create();
                    titulo.setTitle("SEGUIR");
                    titulo.show();

                }
            });
            recyclerUsers.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexion con el servidor" + response, Toast.LENGTH_LONG).show();
        }


    }



    //metodos

    private void ejecutarSerivcio(String URL, final String userFollowing, final User userLogged){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idUser", Integer.toString(userLogged.getIdUser()));
                parametros.put("following",userFollowing);



                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}