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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Car> cars = new ArrayList<>();


    public RecyclerAdapter(Context context, List<Car> cars) {
        this.mContext = context;
        this.cars = cars;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mPrice;
        private ImageView mImageView;
        private LinearLayout mContainer;

        public MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mImageView = view.findViewById(R.id.image1);
            mPrice = view.findViewById(R.id.price_daily);
            mContainer = view.findViewById(R.id.car_container);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.car_list_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Car car = cars.get(position);

        holder.mPrice.setText("Rwf " + car.getDaily());
        holder.mTitle.setText(car.getTitle());
        Glide.with(mContext).load(car.getImage3()).into(holder.mImageView);

        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailedCarActivity.class);

                intent.putExtra("title", car.getTitle());
                intent.putExtra("image1", car.getImage1());
                intent.putExtra("image2", car.getImage2());
                intent.putExtra("image3", car.getImage3());
                intent.putExtra("daily", car.getDaily());
                intent.putExtra("monthly", car.getMonthly());
                intent.putExtra("engine", car.getEngine());
                intent.putExtra("tank", car.getTank());
                intent.putExtra("user", car.getUser());
                intent.putExtra("rent_id",car.getRent_id());
                intent.putExtra("phone",car.getPhone());
                intent.putExtra("address",car.getAddress());
                intent.putExtra("hourprice",car.getHourp());
                intent.putExtra("car_id",car.getCar_id());


                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}