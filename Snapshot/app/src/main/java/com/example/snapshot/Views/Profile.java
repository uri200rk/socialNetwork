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
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Fragment of user profile view
 * @author oriol
 * @version 1.0
 */
public class Profile extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    //--- Declarations of elements ---

    EditText nick, fullName, mail;
    Button btnEditUser, btnSave, btnDelete;
    ImageButton btnGallery, btnFollowers;

    //upload publications variables
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    ArrayList<Publication> listPublications;
    ArrayList<Follow> listFollow;
    RecyclerView recyclerView;

    Boolean mostrarList = false;


    //configurations for inflate fragment
    /**
     * create instance for inflate fragment
     * @return this fragment
     */
    public static Profile newInstance(){
        return new Profile();
    }
    //--- End configurations for inflate fragment ---

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    /**
     * initialize elements and call methods
     * @param inflater
     * @param container
     * @param savedInstance
     * @return view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //--- initialize elements ---

        //ArrayLists
        listPublications = new ArrayList<>();
        listFollow = new ArrayList<>();

        //recycerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_Pub_User);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        //declare elements
        nick = (EditText) view.findViewById(R.id.edtNick);
        fullName = (EditText) view.findViewById(R.id.edtFullName);
        mail = (EditText) view.findViewById(R.id.edtMail);

        //request
        request = Volley.newRequestQueue(getContext());

        //--- Fin initialize elements ---

        //call method
        loadPublications();

        //get user logged
        MainActivity mainActivity = new MainActivity();
        final User userLogged = mainActivity.getUser();
        //end get user logged


        //bloqued writed user data
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


        //--- Declare listeners buttons ---

        //btn edit user
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //permision for write in user data
                nick.setEnabled(true);
                fullName.setEnabled(true);
                mail.setEnabled(true);

                //control visibility butons
                btnEditUser.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.VISIBLE);

                //set background color
                nick.setBackgroundColor(Color.parseColor("#93E7FF"));
                fullName.setBackgroundColor(Color.parseColor("#93E7FF"));
                mail.setBackgroundColor(Color.parseColor("#93E7FF"));

                //put cursor end word and requestFocus
                nick.setSelection(nick.length());
                nick.requestFocus();

            }
        });

        //btn save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validacion existencia follow

                Thread tr=new Thread(){
                    @Override
                    public void run() {
                        final String res=validateUser(nick.getText().toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r=objJASON(res);
                                if(r>0){        //case exist user name
                                    Toast.makeText(getActivity(),R.string.nombreNoDisponible,Toast.LENGTH_SHORT).show();
                                    nick.setText(userLogged.getNick());
                                }else{          //case don't exist use name
                                    updateUser("http://uri200rk.alwaysdata.net:80/webService/update_user.php", userLogged);
                                    Intent i=new Intent(getActivity(), MainActivity.class);
                                    startActivity(i);

                                }
                            }
                        });
                    }
                };
                tr.start(); //star process

                //bloqued write user data
                nick.setEnabled(false);
                fullName.setEnabled(false);
                mail.setEnabled(false);

                //control visibility butons
                btnEditUser.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);

                //set background invisible
                nick.setBackground(null);
                fullName.setBackground(null);
                mail.setBackground(null);

            }
        });

        //btn delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                alerta.setMessage(R.string.eliminacionCuenta)
                        .setCancelable(false)
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser("http://uri200rk.alwaysdata.net:80/webService/delete_user.php", userLogged);

                                //go activity login
                                Intent i=new Intent(getActivity(), MainActivity.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog titulo = alerta.create();
                titulo.setTitle(R.string.eliminar);
                titulo.show();

            }
        });

        //btn see my gallery
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnFollowers.setBackgroundColor(Color.TRANSPARENT);
                btnGallery.setBackgroundColor(Color.parseColor("#73EBEBEB"));

                //call method
                loadPublications();
            }
        });

        //btn see my followers
        btnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnFollowers.setBackgroundColor(Color.parseColor("#73EBEBEB"));
                btnGallery.setBackgroundColor(Color.TRANSPARENT);

                //call method
                loadFollowers();
            }
        });

        //--- End declare listeners buttons ---

        return view;
    }


    //--- Methods ---

    /**
     * Method for update user data
     * @param URL
     * @param user
     */
    private void updateUser(String URL, final User user){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), R.string.operacionExitosa, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),R.string.errorConectar,Toast.LENGTH_SHORT).show();
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

    /**
     * Method for delete user
     * @param URL
     * @param user
     */
    private void deleteUser(String URL, final User user){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), R.string.operacionExitosa, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),getString(R.string.errorConectar),Toast.LENGTH_SHORT).show();
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

    /**
     * Method for show own posts
     */
    private void loadPublications() {
        mostrarList = false;

        String url = "http://uri200rk.alwaysdata.net/webService/consulta_publicaciones.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }


    /**
     * Method for show own followers
     */
    private void loadFollowers() {
        mostrarList = true;

        String url = "http://uri200rk.alwaysdata.net/webService/consulta_lista_follow.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }


    /**
     * case of error consult own post or own followers
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(), getString(R.string.sinDatos), Toast.LENGTH_LONG).show();
        Log.d("ERROR: ", error.toString());

    }

    /**
     * fill arraylist of users records or follow records
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        //get user logged
        MainActivity mainActivity = new MainActivity();
        final User userLogged = mainActivity.getUser();
        //fin get user logged

        //clear arrayList
        listPublications.clear();

        //case see your publications
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
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(getContext(), getString(R.string.errorConectar), Toast.LENGTH_LONG).show();
            }



        }else if (mostrarList){  //case see your followings

            //clar arrayList
            listFollow.clear();

            JSONArray json = response.optJSONArray("follow");

            try {

                for (int i = 0; i < json.length(); i++) {

                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);

                    if (jsonObject.optInt("idUser") == userLogged.getIdUser()) {    //filter for your user

                        listFollow.add(new Follow (jsonObject.optString("nick"), jsonObject.optInt("idUser")));

                    }

                }

                //load follows in recyclerView
                FollowAdapter adapter = new FollowAdapter((listFollow));
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(getContext(), getString(R.string.errorConectar), Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * Method verifies existence of edtUser
     * @param nick
     * @return
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
     * @return number of follows that exist
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

    //--- End methods ---

}