package com.example.snapshot.Views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.Clases.User;
import com.example.snapshot.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class UploadMedia extends AppCompatActivity {

    //--- Declarations of elements ---

    //get a date and hours
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    String formattedDate = df.format(c.getTime());


    Button btnSubir;
    ImageButton btnInicio;
    ImageView imagen;
    EditText titulo, descripcion;

    //upload image
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String UPLOAD_URL = "http://uri200rk.alwaysdata.net/webService/upload.php";
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";


    //--- Configurations for inflate fragment ---

    public static UploadMedia newInstance(){
        return new UploadMedia();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_list_publication, container, false);

        return view;
    }

    //--- End configurations for inflate fragment ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media);

        //--- initialize elements ---

        //get user logged
        MainActivity mainActivity = new MainActivity();
        final User userLogged = mainActivity.getUser();
        //fin get user logged


        imagen = findViewById(R.id.imagen);
        titulo = findViewById(R.id.titulo);
        btnSubir = findViewById(R.id.btnSubir);
        btnInicio = findViewById(R.id.btnInicio);
        descripcion = findViewById(R.id.descripcion);


        //call method choose image
        showFileChooser();

        //--- Declare listeners buttons ---

        //btn go main activity
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });


        //btn upload image

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarSerivcio("http://uri200rk.alwaysdata.net:80/webService/insertar_publicacion.php", userLogged);

                uploadImagen();

                goHome(1000);

            }
        });

        //--- End declare listeners buttons ---



    }

    //--- Methods ---

    //go activity home
    public void goHome (int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        }, milisegundos);
    }

    //converts the image to a string
    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100,baos);
        byte[] imagenBytes = baos.toByteArray();
        String encodedImagen = Base64.encodeToString(imagenBytes, Base64.DEFAULT);

        return encodedImagen;
    }

    //upload image on webServer
    public void uploadImagen() {
        final ProgressDialog cargando = ProgressDialog.show(this,getString(R.string.upload),getString(R.string.esperePorfavor));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cargando.dismiss();
                Toast.makeText(UploadMedia.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cargando.dismiss();
                Toast.makeText(UploadMedia.this, getString(R.string.errorConectar), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = titulo.getText().toString().trim();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, formattedDate);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //para seleccionar una imagen
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,getString(R.string.selecionarImagen)), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagen.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //execute sql sentence
    private void ejecutarSerivcio(String URL, final User user){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), getString(R.string.operacionExitosa), Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String > parametros=new HashMap<String, String>();
                parametros.put("idUser", Integer.toString(user.getIdUser()));
                parametros.put("nick", user.getNick());
                parametros.put("title",titulo.getText().toString());
                parametros.put("description",descripcion.getText().toString());
                parametros.put("idMedia", formattedDate);
                parametros.put("likes","0");
                parametros.put("date",formattedDate);


                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //--- End methods ---

}
