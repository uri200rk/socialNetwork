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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.Clases.User;
import com.example.snapshot.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    //--- Declarations of elements ---
    EditText edtUser, edtPassword;
    Button btnLogin;
    TextView t_signUp;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    //user object
    static User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        request = Volley.newRequestQueue(this);

        //--- initialize elements ---

        edtUser = (EditText) findViewById(R.id.loginNick);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.signUp);
        t_signUp = (TextView) findViewById(R.id.t_signIn);

        //--- Buttons ---

        //Button login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread tr=new Thread(){
                    @Override
                    public void run() {
                        final String res=validateUser(edtUser.getText().toString(), edtPassword.getText().toString());
                        getObjUser();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=objJASON(res);
                                if(r>0){     //case exist user
                                    Intent i=new Intent(getApplicationContext(), Menu.class);
                                    startActivity(i);
                                }else{      //case error user or password
                                    Toast.makeText(getApplicationContext(),R.string.usuario_pass_incorrect,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
        });


        //Text buton register
        t_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });

    }

    //--- Methods ---

    //Method check dates
    public String validateUser(String user, String password){
        String parametros = "nick="+user+"&password="+password;
        HttpURLConnection conection = null;
        String respuesta = "";
        try {
            URL url = new URL("http://uri200rk.alwaysdata.net:80/webService/validar_usuario.php");
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

    //End check dates


    //Method get object user

    private void getObjUser() {

        String url = "http://uri200rk.alwaysdata.net/webService/uploadUserReggistered.php?nick="+edtUser.getText();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        Log.d("ERROR: ", error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {

        JSONArray json = response.optJSONArray("user");
        try {

            for (int i = 0; i<json.length(); i++){

                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                //create object logged in current user
                user = new User(jsonObject.optInt("idUser"),jsonObject.optString("fullName"),jsonObject.optString("nick"), jsonObject.optString("mail"), jsonObject.optString("password"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.errorConectar, Toast.LENGTH_LONG).show();
        }

    }


    //to get user object from another class
    public static User getUser(){
        return user;
    }

    //End method get user logged

}
