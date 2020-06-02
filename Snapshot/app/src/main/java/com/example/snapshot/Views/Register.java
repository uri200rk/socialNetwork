package com.example.snapshot.Views;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.R;
import org.json.JSONArray;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Fragment to register new user
 * @author oriol
 * @version 1.0
 */
public class Register extends AppCompatActivity {

    //--- Declarations of elements ---

    EditText fullName,username,mail,password;
    Button btnInsertar;
    TextView t_signIn;

    //characters checking mail
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    /**
     * initialize elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //--- initialize elements ---

        t_signIn = (TextView) findViewById(R.id.t_signIn);
        fullName=(EditText)findViewById(R.id.loginNick);
        username=(EditText)findViewById(R.id.nick);
        mail=(EditText)findViewById(R.id.mail);
        password=(EditText) findViewById(R.id.password);
        btnInsertar=(Button)findViewById(R.id.signUp);

        //--- Declare listeners buttons ---

        //btn insert user
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread tr=new Thread(){
                    @Override
                    public void run() {
                        final String res=validateUser(username.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=objJASON(res);
                                if(r>0){     //case exist user
                                    Toast.makeText(getApplicationContext(),R.string.nombreNoDisponible,Toast.LENGTH_SHORT).show();
                                }else{      //case error user or password

                                    if(validateEmail() == true) {   // validate correct form mail
                                        ejecutarSerivcio("http://uri200rk.alwaysdata.net:80/webService/insertar_usuario.php");
                                        Intent i=new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                    }
                                }
                            }
                        });
                    }
                };
                tr.start();

            }
        });

        //btn log in
        t_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


    }

    //--- Error control ---

    /**
     * Method for mail error control
     * @return
     */
    private boolean validateEmail() {
        String emailInput = mail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            mail.setError(getString(R.string.campoObligatorio));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            mail.setError(getString(R.string.mailValido));
            return false;
        } else {
            mail.setError(null);
            return true;
        }
    }

    // --- End error control ---


    //--- Methods ---

    /**
     * Method check dates
     * @param nick
     * @return answer
     */
    public String validateUser(String nick){
        String parametros = "nick="+nick;
        HttpURLConnection conection = null;
        String respuesta = "";
        try {
            URL url = new URL("http://uri200rk.alwaysdata.net:80/webService/validar_edtUsuario.php");
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

    /**
     * Get answer validateUser
     * @param respuesta
     * @return number of users that exist
     */
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

    /**
     * Method for insert new user
     * @param URL
     */
    private void ejecutarSerivcio(String URL){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), R.string.operacionExitosa, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),R.string.errorConectar,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("fullName",fullName.getText().toString());
                parametros.put("nick",username.getText().toString());
                parametros.put("mail",mail.getText().toString());
                parametros.put("password",password.getText().toString());


                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}