package com.example.snapshot.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.Adapter.UsersAdapter;
import com.example.snapshot.Clases.User;
import com.example.snapshot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    //DECLARACION DE ELEMENTOS
    EditText edtUser, edtPassword;
    Button btnLogin;
    TextView t_signUp;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    static User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtener usuario

        request = Volley.newRequestQueue(this);


        //fin obtener usuario

        //INICIALIZACION DE ELEMENTOS

        edtUser = (EditText) findViewById(R.id.loginNick);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.signUp);
        t_signUp = (TextView) findViewById(R.id.t_signIn);

        //BOTONES

        //---boton login accion---
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread tr=new Thread(){
                    @Override
                    public void run() {
                        final String res=validarUsuario(edtUser.getText().toString(), edtPassword.getText().toString());
                        loadWebService();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=objJASON(res);
                                if(r>0){
                                    Intent i=new Intent(getApplicationContext(), Menu.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecto",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
        });


        //--boton texto registrarse--
        t_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });



    }

    //METODOS

    //---Metodo comprueba datos---
    public String validarUsuario(String user, String password){
        String parametros = "nick="+user+"&password="+password;
        HttpURLConnection conection = null;
        String respuesta = "";
        try {
            URL url = new URL("http://192.168.1.16:80/webService/validar_usuario.php");
            conection = (HttpURLConnection)url.openConnection();
            conection.setRequestMethod("POST");
            conection.setRequestProperty("Content-Lenght",""+Integer.toString(parametros.getBytes().length));

            conection.setDoOutput(true);
            DataOutputStream wr= new DataOutputStream(conection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream=new Scanner(conection.getInputStream());

            while (inStream.hasNextLine()){
                respuesta+=(inStream.nextLine());
            }

        }catch (Exception e){}
        return respuesta.toString();
    }

    public int objJASON(String respuesta){
        int res=0;
        try {
            JSONArray json = new JSONArray(respuesta);
            if(json.length() > 0){
                res=1;
            }

        }catch (Exception e){}

        return res;
    }

    //---Fin comprueba datos---


    //---Metodo obtenerObjUser---


    private void loadWebService() {

        String url = "http://192.168.1.16/webService/uploadUserReggistered.php?nick="+edtUser.getText();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(this, "error al conectar" + error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("ERROR: ", error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {

        JSONArray json = response.optJSONArray("user");
        try {

            for (int i = 0; i<json.length(); i++){

                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                //listUsers.add(new User (jsonObject.optString("nick")));
              user = new User(jsonObject.optInt("idUser"),jsonObject.optString("fullName"),jsonObject.optString("nick"), jsonObject.optString("mail"), jsonObject.optString("password"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se ha podido establecer conexion con el servidor" + response, Toast.LENGTH_LONG).show();
        }


    }



    public static User getUser(){
        return user;
    }

    //--fin metodo obtener usuario logueado

}
