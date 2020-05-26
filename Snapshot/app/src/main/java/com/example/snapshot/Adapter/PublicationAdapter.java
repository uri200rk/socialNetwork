package com.example.snapshot.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapshot.Clases.Publication;
import com.example.snapshot.R;

import java.util.List;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationHolder> {

    List<Publication> listPublication;
    RequestQueue request;
    Context context;

    public PublicationAdapter(List<Publication> listPublication, Context context){
        this.listPublication = listPublication;
        this.context = context;
        request = Volley.newRequestQueue(context);
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
    public void onBindViewHolder(@NonNull PublicationAdapter.PublicationHolder holder, int position) {
        holder.nickUser.setText(String.valueOf(listPublication.get(position).getNick()));
        holder.title.setText(listPublication.get(position).getTitle());
        holder.description.setText(listPublication.get(position).getDescription());
        holder.likes.setText(String.valueOf(listPublication.get(position).getLikes()));

        if (listPublication.get(position).getIdMedia() != null){

            //holder.idMedia.setImageBitmap(listPublication.get(position).getIdMedia());
            loadImageWebService(listPublication.get(position).getIdMedia(), holder);

        } else {

            holder.idMedia.setImageResource(R.drawable.imagen_error);

        }

    }

    private void loadImageWebService(String idMedia, final PublicationHolder holder) {

        String urlImage = "http://192.168.1.16/webService/upload/" + idMedia + ".png";
        urlImage = urlImage.replace(" " , "%20");

        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                holder.idMedia.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(imageRequest);


    }

    @Override
    public int getItemCount() {
        return listPublication.size();
    }

    public class PublicationHolder extends RecyclerView.ViewHolder {

        TextView nickUser, title, description, likes;
        ImageView idMedia;

        public PublicationHolder(View itemView) {
            super(itemView);
            nickUser= (TextView) itemView.findViewById(R.id.edtNick);
            title= (TextView) itemView.findViewById(R.id.title);
            description= (TextView) itemView.findViewById(R.id.description);
            likes= (TextView) itemView.findViewById(R.id.likes);
            idMedia= (ImageView) itemView.findViewById(R.id.idMedia);


        }
    }
}
