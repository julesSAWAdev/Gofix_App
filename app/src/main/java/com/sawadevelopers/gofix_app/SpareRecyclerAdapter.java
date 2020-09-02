package com.sawadevelopers.gofix_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SpareRecyclerAdapter extends RecyclerView.Adapter<SpareRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<spare> spares = new ArrayList<>();

    public SpareRecyclerAdapter(Context context, List<spare> spares){
        this.mContext = context;
        this.spares = spares;

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
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spare_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final spare spare = spares.get(position);

        holder.mPrice.setText("Rwf "+spare.getSpareprice());
        holder.mTitle.setText(""+spare.getSparename());
        Glide.with(mContext).load(spare.getSpareimage()).into(holder.mImageView);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FindSpare.class);

                intent.putExtra("title", spare.getSparename());
                intent.putExtra("image", spare.getSpareimage());
                intent.putExtra("price", spare.getSpareprice());
                intent.putExtra("description", spare.getSparedescription());
                intent.putExtra("spareid", spare.getSpareid());
                intent.putExtra("userid", spare.getUserid());
                intent.putExtra("phone", spare.getPhone());
                intent.putExtra("address", spare.getAddress());

                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return spares.size();
    }

}
