package com.example.bloodbank4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank4.R;
import com.example.bloodbank4.container.Donor;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Handler> {

    private List<Donor> list;
    private Context context;

    public CustomAdapter(Context context,List<Donor> list) {
        this.context=context;
        this.list = list;
    }

    @NonNull
    @Override
    public Handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(context).inflate(R.layout.receive_single_item,parent,false);
        return new Handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Handler holder, int position) {
        TextView bloodgpView =holder.bgp;
        TextView nameView = holder.name;
        TextView addressView =holder.address;
        CircularImageView callImageView = holder.call;
        CircularImageView gmapImageView = holder.gmap;

        String nameofdonor = list.get(position).getName();
        final String phoneofdonor = list.get(position).getPhone();
        String blood = list.get(position).getBlood_group();

        bloodgpView.setText(blood);
        nameView.setText(nameofdonor);
        addressView.setText(phoneofdonor);
        callImageView.setImageResource(R.drawable.call);
        gmapImageView.setImageResource(R.drawable.google_maps);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Handler extends RecyclerView.ViewHolder {

        CircularImageView call, gmap;
        TextView name, address, bgp;

        public Handler(@NonNull View itemView) {
            super(itemView); bgp =  itemView.findViewById(R.id.receive_single_bloodgp);
            call =  itemView.findViewById(R.id.receive_single_call);
            gmap = itemView.findViewById(R.id.receive_single_gmap);
            name =  itemView.findViewById(R.id.receive_single_name);
            address = itemView.findViewById(R.id.receive_single_address);
        }
    }
}
