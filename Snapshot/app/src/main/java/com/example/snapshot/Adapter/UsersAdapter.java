package com.example.snapshot.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snapshot.Clases.User;
import com.example.snapshot.R;

import java.util.List;

public class UsersAdapter
        extends RecyclerView.Adapter<UsersAdapter.UsersHolder>
        implements View.OnClickListener{

    List<User> listUsers;
    private View.OnClickListener listener;

    public UsersAdapter(List<User> listUsers){
        this.listUsers = listUsers;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_usuarios_list,parent,false);

        vista.setOnClickListener(this);

        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new UsersHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, int position) {

        holder.nickUser.setText(listUsers.get(position).getNick().toString());

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }


    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        if(listener != null){
            listener.onClick(v);
        }

    }

    public class UsersHolder extends RecyclerView.ViewHolder {

        TextView nickUser;

        public UsersHolder(View itemView) {
            super(itemView);
            nickUser= (TextView) itemView.findViewById(R.id.edtNick);


        }
    }

}
