package com.example.snapshot.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ListUsers extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    //--- Declarations of elements ---

    RecyclerView recyclerUsers;
    ArrayList<User> listUsers;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    SearchView buscador;


    //--- Configurations for inflate fragment ---

    public static ListUsers newInstance(){
        return new ListUsers();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    //--- End configurations for inflate fragment ---


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_list_users, container, false);

        //--- initialize elements ---

        //arrayList publication
        listUsers = new ArrayList<>();

        //recyclerView publications
        recyclerUsers = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerUsers.setHasFixedSize(true);

        //searchView
        buscador = (SearchView) view.findViewById(R.id.buscador);

        //request
        request = Volley.newRequestQueue(getContext());

        //--- Fin initialize elements ---

        //call method
        loadWebService();

        return view;
    }

    //--- METHODS ---

    //Method for consult list users registered in Snapshot
    private void loadWebService() {

        String url = "http://uri200rk.alwaysdata.net/webService/consultar_lista_usuario.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);

    }



    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(), R.string.errorConectar + error.toString(), Toast.LENGTH_LONG).show();
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

                if (!jsonObject.optString("idUser").equals(Integer.toString(userLogged.getIdUser()))){  //all records except own

                    listUsers.add(new User (jsonObject.optString("nick"), jsonObject.optInt("idUser")));

                }

            }


            final UsersAdapter adapter = new UsersAdapter((listUsers));
            adapter.setOnclickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    final int userFollowing = listUsers.get(recyclerUsers.getChildAdapterPosition(v)).getIdUser();


                    //prepare alertDialog

                    final AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
                    alerta.setMessage(getString(R.string.desearSeguir) + " " +  listUsers.get(recyclerUsers.getChildAdapterPosition(v)).getNick() + "?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {    //case option yes
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String url = "http://uri200rk.alwaysdata.net/webService/insertar_follow.php";
                                    insertFollow(url, userFollowing, userLogged);

                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {    //case option no
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });



                    //validate existence follow

                    Thread tr=new Thread(){
                        @Override
                        public void run() {
                            final String res=validateFollow(userLogged.getIdUser(), userFollowing);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int r=objJASON(res);
                                    if(r>0){    //case exist name user
                                        Toast.makeText(getActivity(),R.string.yaSigueUsuario,Toast.LENGTH_SHORT).show();
                                    }else{      //case don't exist name user
                                        AlertDialog titulo = alerta.create();
                                        titulo.setTitle(R.string.BtnSeguirUsuari);
                                        titulo.show();

                                    }
                                }
                            });
                        }
                    };
                    tr.start(); //start process

                }
            });

            //add registers in recyclerView
            recyclerUsers.setAdapter(adapter);

            //buton searcher users
            buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });

        } catch (JSONException e) {
            Toast.makeText(getContext(), R.string.errorConectar, Toast.LENGTH_LONG).show();
        }


    }

    // End method for consult list users registered in Snapshot


    // Method for insert follow
    private void insertFollow(String URL, final int userFollowing, final User userLogged){

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
                parametros.put("idUser", Integer.toString(userLogged.getIdUser()));
                parametros.put("following",Integer.toString(userFollowing));

                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    //---Method check existence follow---

    public String validateFollow(int idUser, int following){
        String parametros = "idUser="+idUser+"&following="+following;
        HttpURLConnection conection = null;
        String respuesta = "";
        try {
            URL url = new URL("http://uri200rk.alwaysdata.net:80/webService/validar_follow.php");
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

}