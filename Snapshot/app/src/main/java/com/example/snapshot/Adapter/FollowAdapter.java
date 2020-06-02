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

/**
 * The adapter of follow
 * @author oriol
 * @version 1.0
 */
public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowHolder> implements View.OnClickListener{

    //--- Declarations of elements ---

    List<Follow> listFollow;
    private View.OnClickListener listener;

    /**
     * Adapter constructor
     * @param listFollow
     */
    public FollowAdapter(List<Follow> listFollow){
        this.listFollow = listFollow;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * create view holder
     * @param parent
     * @param viewType
     * @return follow holder
     */
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

    /**
     * Set text nameFollowing by arrayList registry
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull FollowHolder holder, int position) {

        holder.nameFollowing.setText(String.valueOf(listFollow.get(position).getNameFollowing()));

    }

    /**
     * Get size arrayList
     * @return
     */
    @Override
    public int getItemCount() {
        return listFollow.size();
    }


    /**
     * Declarations of elements holder
     */
    public class FollowHolder extends RecyclerView.ViewHolder {

        TextView nameFollowing;

        public FollowHolder(View vista) {
            super(vista);
            nameFollowing= (TextView) itemView.findViewById(R.id.edtNick);
        }
    }
}
