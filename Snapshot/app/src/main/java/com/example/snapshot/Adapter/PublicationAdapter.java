package com.example.snapshot.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.Clases.Publication;
import com.example.snapshot.R;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationHolder> implements View.OnClickListener {

    //--- Declarations of elements ---

    List<Publication> listPublication;
    RequestQueue request;
    Context context;

    //adapter constructor
    public PublicationAdapter(List<Publication> listPublication, Context context){
        this.listPublication = listPublication;
        this.context = context;
        request = Volley.newRequestQueue(context);
    }

    @Override
    public void onClick(View v) {

    }

    //--- Declarations of elements holder ---
    public class PublicationHolder extends RecyclerView.ViewHolder {

        TextView nickUser, title, description, likes;
        ImageView idMedia;
        ImageButton btnLike;

        public PublicationHolder(View itemView) {

            super(itemView);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);
            nickUser= (TextView) itemView.findViewById(R.id.edtNick);
            title= (TextView) itemView.findViewById(R.id.title);
            description= (TextView) itemView.findViewById(R.id.description);
            likes= (TextView) itemView.findViewById(R.id.likes);
            idMedia= (ImageView) itemView.findViewById(R.id.idMedia);

        }
    }


    @NonNull
    @Override
    public PublicationAdapter.PublicationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_publication,parent,false);

        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);


        return new PublicationAdapter.PublicationHolder(vista);
    }


    @Override
    public void onBindViewHolder(@NonNull PublicationAdapter.PublicationHolder holder, final int position) {
        holder.nickUser.setText(String.valueOf(listPublication.get(position).getNick()));
        holder.title.setText(listPublication.get(position).getTitle());
        holder.description.setText(listPublication.get(position).getDescription());
        holder.likes.setText(String.valueOf(listPublication.get(position).getLikes()));

        //Button like
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("entrooo" + listPublication.get(position).getNick());

                addLikes("http://uri200rk.alwaysdata.net:80/webService/addLike.php", listPublication , position);

            }
        });


        if (listPublication.get(position).getIdMedia() != null){

            loadImageWebService(listPublication.get(position).getIdMedia(), holder);

        } else {

            holder.idMedia.setImageResource(R.drawable.imagen_error);

        }

    }

    //--- Methods ---

    //method load image from web server
    private void loadImageWebService(String idMedia, final PublicationHolder holder) {

        String urlImage = "http://uri200rk.alwaysdata.net/webService/upload/" + idMedia + ".png";
        urlImage = urlImage.replace(" " , "%20");

        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                holder.idMedia.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, R.string.errorConectar, Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);


    }

    @Override
    public int getItemCount() {
        return listPublication.size();
    }


    //add likes
    private void addLikes(String URL, final List<Publication> idPublication, final int position){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, R.string.dadoLike, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,R.string.errorConectar,Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("idPublication", Integer.toString(listPublication.get(position).getIdPublication()));
                parametros.put("likes", Integer.toString(listPublication.get(position).getLikes()));

                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    //--- End methods ---

}
