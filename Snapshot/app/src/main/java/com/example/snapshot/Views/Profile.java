package com.example.snapshot.Views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.snapshot.Adapter.FollowAdapter;
import com.example.snapshot.Adapter.PublicationAdapter;
import com.example.snapshot.Clases.Follow;
import com.example.snapshot.Clases.Publication;
import com.example.snapshot.Clases.User;
import com.example.snapshot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Profile extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    //varibales

    EditText nick, fullName, mail;
    Button btnEditUser, btnSave, btnDelete;
    ImageButton btnGallery, btnFollowers;

    //upload publications variables
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    ArrayList<Publication> listPublications;
    ArrayList<Follow> listFollow;
    RecyclerView recycler_Pub_User;



    Boolean mostrarList = false;


    //configurations with inflate fragment
    public static Profile newInstance(){
        return new Profile();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //upload publications

        listPublications = new ArrayList<>();
        listFollow = new ArrayList<>();

        recycler_Pub_User = (RecyclerView) view.findViewById(R.id.recycler_Pub_User);
        recycler_Pub_User.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler_Pub_User.setHasFixedSize(true);

        request = Volley.newRequestQueue(getContext());

        uploadPublications();

        //get user logged
        MainActivity mainActivity = new MainActivity();
        final User userLogged = mainActivity.getUser();
        //fin get user logged

        //declare elements
        nick = (EditText) view.findViewById(R.id.edtNick);
        fullName = (EditText) view.findViewById(R.id.edtFullName);
        mail = (EditText) view.findViewById(R.id.edtMail);

        //bloqued writed
        nick.setEnabled(false);
        fullName.setEnabled(false);
        mail.setEnabled(false);

        //set user data
        nick.setText(userLogged.getNick());
        fullName.setText(userLogged.getFullName());
        mail.setText(userLogged.getMail());


        //declare buttons

        btnEditUser = (Button) view.findViewById(R.id.btnEditUser);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        btnSave.setVisibility(View.INVISIBLE);
        btnGallery = (ImageButton) view.findViewById(R.id.btnGallery);
        btnFollowers = (ImageButton) view.findViewById(R.id.btnFollowers);


        //declare listeners buttons

        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nick.setEnabled(true);
                fullName.setEnabled(true);
                mail.setEnabled(true);

                btnEditUser.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.VISIBLE);

                //mostar que se puede editar
                nick.setBackgroundColor(Color.parseColor("#93E7FF"));
                fullName.setBackgroundColor(Color.parseColor("#93E7FF"));
                mail.setBackgroundColor(Color.parseColor("#93E7FF"));

                nick.setSelection(nick.length());
                nick.requestFocus();




            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ejecutarSerivcioUpdate("http://uri200rk.alwaysdata.net:80/webService/update_user.php", userLogged);

                nick.setEnabled(false);
                fullName.setEnabled(false);
                mail.setEnabled(false);

                btnEditUser.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);

                //volver a poner fondo vacio
                nick.setBackground(null);
                fullName.setBackground(null);
                mail.setBackground(null);



            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                alerta.setMessage("Deseas eliminar la cuenta?")
                        .setCancelable(false)
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ejecutarSerivcioDelete("http://uri200rk.alwaysdata.net:80/webService/delete_user.php", userLogged);

                                //ir a pantalla login
                                Intent i=new Intent(getActivity(), MainActivity.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("ELIMINAR");
                titulo.show();

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPublications();
            }
        });

        btnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFollowers();
            }
        });



        return view;
    }




    //METODOS

    //update user
    private void ejecutarSerivcioUpdate(String URL, final User user){

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
                parametros.put("idUser", Integer.toString(user.getIdUser()));
                parametros.put("nick",nick.getText().toString());
                parametros.put("fullName",fullName.getText().toString());
                parametros.put("mail",mail.getText().toString());

                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    //delete user
    private void ejecutarSerivcioDelete(String URL, final User user){

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
                parametros.put("idUser", Integer.toString(user.getIdUser()));

                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    //mostrar publicaciones propias

    private void uploadPublications() {
        mostrarList = false;

        //get user logged
        MainActivity mainActivity = new MainActivity();
        final User userLogged = mainActivity.getUser();
        //fin get user logged

        String url = "http://uri200rk.alwaysdata.net/webService/consulta_publicaciones.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }

    //mostrar followers

    private void uploadFollowers() {
        mostrarList = true;

        //get user logged
        MainActivity mainActivity = new MainActivity();
        final User userLogged = mainActivity.getUser();
        //fin get user logged

        String url = "http://uri200rk.alwaysdata.net/webService/consulta_lista_follow.php";

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

        //vaciamos arrayList
        listPublications.clear();

        //caso de querer ver tus publicaciones
        if (!mostrarList) {
            JSONArray json = response.optJSONArray("publication");

            try {

                for (int i = 0; i < json.length(); i++) {


                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);

                    if (jsonObject.optString("nick").equals(userLogged.getNick())) {
                        listPublications.add(new Publication(jsonObject.optInt("idPublication"), jsonObject.optInt("idUser"), jsonObject.optString("nick"), jsonObject.optInt("likes"), jsonObject.optString("title"),
                                jsonObject.optString("description"), jsonObject.optString("idMedia"), jsonObject.optString("date")));

                    }
                }

                PublicationAdapter adapter = new PublicationAdapter(listPublications, getContext());
                recycler_Pub_User.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "No se ha podido establecer conexion con el servidor" + response, Toast.LENGTH_LONG).show();
            }
        }else if (mostrarList){  //caso ver lista followings

            listFollow.clear();

            JSONArray json = response.optJSONArray("follow");

            try {

                for (int i = 0; i < json.length(); i++) {


                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);

                    if (jsonObject.optString("idUser").equals(Integer.toString(userLogged.getIdUser()))) {
                        listFollow.add(new Follow (jsonObject.optInt("idUser"), jsonObject.optInt("following"), jsonObject.optInt("idFollow") ));

                    }



                }

                FollowAdapter adapter = new FollowAdapter((listFollow));
                recycler_Pub_User.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "No se ha podido establecer conexion con el servidor" + response, Toast.LENGTH_LONG).show();
            }

        }
    }




}