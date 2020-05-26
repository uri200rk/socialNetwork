package com.example.snapshot.Views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.Clases.User;
import com.example.snapshot.R;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;


public class Profile extends Fragment {

    //varibales

    EditText nick, fullName, mail;
    Button btnEditUser, btnSave, btnDelete;



    //configurations with inflate fragment
    public static Profile newInstance(){
        return new Profile();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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


                ejecutarSerivcioUpdate("http://192.168.1.16:80/webService/update_user.php", userLogged);

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
                                ejecutarSerivcioDelete("http://192.168.1.16:80/webService/delete_user.php", userLogged);

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




}