package com.sawadevelopers.gofix_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ElectronicsRecyclerAdapter extends RecyclerView.Adapter<ElectronicsRecyclerAdapter.MyViewHolder>{

    private Context mContext;
    private List<Electronics> electronics = new ArrayList<>();

    public ElectronicsRecyclerAdapter(Context context, List<Electronics> electronics){
        this.mContext = context;
        this.electronics = electronics;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.electronic_list_item,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Electronics electronic = electronics.get(position);

        holder.mPrice.setText("Rwf "+electronic.getSpareprice());
        holder.mTitle.setText(""+electronic.getSparename());
        Glide.with(mContext).load(electronic.getSpareimage()).into(holder.mImageView);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FindElectrinic.class);

                intent.putExtra("title", electronic.getSparename());
                intent.putExtra("image", electronic.getSpareimage());
                intent.putExtra("price", electronic.getSpareprice());
                intent.putExtra("description", electronic.getSparedescription());
                intent.putExtra("spareid", electronic.getSpareid());
                intent.putExtra("userid", electronic.getUserid());
                intent.putExtra("phone", electronic.getPhone());
                intent.putExtra("address", electronic.getAddress());

                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return electronics.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitle,mPrice;
        private ImageView mImageView;
        private LinearLayout mContainer;

        public MyViewHolder(View view){
            super(view);
            mTitle = view.findViewById(R.id.title2);
            mPrice = view.findViewById(R.id.price);
            mImageView = view.findViewById(R.id.imagespare);
            mContainer = view.findViewById(R.id.spare_container);
        }
    }

}
