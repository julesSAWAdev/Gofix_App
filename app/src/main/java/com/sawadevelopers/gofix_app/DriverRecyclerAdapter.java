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

public class DriverRecyclerAdapter extends RecyclerView.Adapter<DriverRecyclerAdapter.MyViewHolder> {


    private Context mContext;
    private List<Driver> drivers = new ArrayList<>();

    public DriverRecyclerAdapter(Context mContext, List<Driver> drivers) {
        this.mContext = mContext;
        this.drivers = drivers;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitle,categories;
        private ImageView mImageView;
        private LinearLayout mContainer;

        public MyViewHolder(View view){
            super(view);
            mTitle = view.findViewById(R.id.name);
            categories = view.findViewById(R.id.categories);
            mImageView = view.findViewById(R.id.imagedriver);
            mContainer = view.findViewById(R.id.driver_container);
        }
    }

    @NonNull
    @Override
    public DriverRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.driver_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverRecyclerAdapter.MyViewHolder holder, int position) {

        final Driver driver = drivers.get(position);

        holder.categories.setText("Categories "+driver.getCategory());
        holder.mTitle.setText(""+driver.getDriver_name());
        Glide.with(mContext).load(driver.getImage()).into(holder.mImageView);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(mContext, FindDriver.class);

                intent.putExtra("name", driver.getDriver_name());
                intent.putExtra("id", driver.getDriver_id());
                intent.putExtra("image", driver.getImage());
                intent.putExtra("categories", driver.getCategory());
                intent.putExtra("dlnumber", driver.getDlnumber());
                intent.putExtra("idnumber", driver.getDriver_id());
                intent.putExtra("created", driver.getCreated_at());
                intent.putExtra("phone", driver.getPhone());
                intent.putExtra("address", driver.getAdress());

                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }
}
