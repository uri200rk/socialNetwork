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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    //caracteres comprovacion mail
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

    //DECLARACION DE ELEMENTOS
    EditText fullName,username,mail,password;
    Button btnInsertar;
    TextView t_signIn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //INICIALIZACION DE ELEMENTOS

        t_signIn = (TextView) findViewById(R.id.t_signIn);

        fullName=(EditText)findViewById(R.id.loginNick);
        username=(EditText)findViewById(R.id.nick);
        mail=(EditText)findViewById(R.id.mail);
        password=(EditText) findViewById(R.id.password);

        btnInsertar=(Button)findViewById(R.id.signUp);



        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail() == true) {
                    ejecutarSerivcio("http://uri200rk.alwaysdata.net:80/webService/insertar_usuario.php");
                }
            }
        });

        //--boton iniciar sesion--
        t_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


    }

    /*controles de errores*/

    private boolean validateEmail() {
        String emailInput = mail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            mail.setError("Campo obligatorio");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            mail.setError("Porfavor introduce un mail válido");
            return false;
        } else {
            mail.setError(null);
            return true;
        }
    }

    /*fin controles de errores*/

    //METODOS

    private void ejecutarSerivcio(String URL){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
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