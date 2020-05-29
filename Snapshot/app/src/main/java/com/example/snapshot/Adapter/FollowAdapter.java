package com.example.snapshot.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snapshot.Clases.Follow;
import com.example.snapshot.R;

import java.util.List;

public class FollowAdapter
        extends RecyclerView.Adapter<FollowAdapter.FollowHolder>
        implements View.OnClickListener{

    List<Follow> listFollow;
    private View.OnClickListener listener;

    public FollowAdapter(List<Follow> listFollow){
        this.listFollow = listFollow;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public FollowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_usuarios_list,parent,false);

        vista.setOnClickListener(this);

        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new FollowHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowHolder holder, int position) {

        holder.idUser.setText(String.valueOf(listFollow.get(position).getIdUser()));


    }

    @Override
    public int getItemCount() {
        return listFollow.size();
    }


    public class FollowHolder extends RecyclerView.ViewHolder {

        TextView idUser;

        public FollowHolder(View vista) {
            super(vista);
            idUser= (TextView) itemView.findViewById(R.id.edtNick);
        }
    }
}