package com.example.snapshot.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.snapshot.Clases.User;
import com.example.snapshot.R;
import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> implements View.OnClickListener, Filterable {

    //--- Declarations of elements ---
    List<User> listUsers;
    List<User> listUsersFull;
    private View.OnClickListener listener;

    //adapter constructor
    public UsersAdapter(List<User> listUsers){
        this.listUsers = listUsers;
        listUsersFull = new ArrayList<>(listUsers);
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

    //--- Declarations of elements holder ---
    public class UsersHolder extends RecyclerView.ViewHolder {

        TextView nickUser;

        public UsersHolder(View itemView) {
            super(itemView);
            nickUser= (TextView) itemView.findViewById(R.id.edtNick);


        }
    }

    //--- Methods ---

    //filter
    @Override
    public Filter getFilter() {
        return filteredList;
    }

    //filter in recyclerView by users
    private Filter filteredList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null ||   constraint.length() == 0){
                filteredList.addAll(listUsersFull);
            }else {
                String filteredPattern = constraint.toString().toLowerCase().trim();

                for ( User item : listUsersFull){

                    if(item.getNick().toLowerCase().contains(filteredPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        //change data recycerView
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listUsers.clear();
            listUsers.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    //--- End methods ---

}
