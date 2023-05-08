package com.runora_dev.runoraf.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.runora_dev.runoraf.R;
import com.runora_dev.runoraf.Activity.DetailActicity;
import com.runora_dev.runoraf.Model.ItemDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class CustAdapter extends RecyclerView.Adapter<CustAdapter.MyViewHolder> {
    Context context;
    List<ItemDb> itemList;
    List<ItemDb> itemListcopy = new ArrayList<>();
    LayoutInflater layoutInflater;

    public CustAdapter(Context context, List<ItemDb> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.layoutInflater = LayoutInflater.from(context);
        this.itemListcopy.addAll(itemList);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.itemview, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(itemList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActicity.class);
                intent.putExtra("from", "adapter");
                intent.putExtra("name", itemList.get(position).getName());
                intent.putExtra("sodiumMg", itemList.get(position).getSodiumMg());
                intent.putExtra("potassiumMg", itemList.get(position).getPotassiumMg());
                intent.putExtra("cholesterolMg", itemList.get(position).getCholesterolMg());
                intent.putExtra("sugarG", Double.parseDouble(itemList.get(position).getSugarG()));
                intent.putExtra("fiberG", Double.parseDouble(itemList.get(position).getFiberG()));
                intent.putExtra("servingSizeG", Double.parseDouble(itemList.get(position).getServingSizeG()));
                intent.putExtra("fatSaturatedG", Double.parseDouble(itemList.get(position).getFatSaturatedG()));
                intent.putExtra("fatTotalG", Double.parseDouble(itemList.get(position).getFatTotalG()));
                intent.putExtra("calories", Double.parseDouble(itemList.get(position).getCalories()));
                intent.putExtra("proteinG", Double.parseDouble(itemList.get(position).getProteinG()));
                intent.putExtra("carbohydratesTotalG", Double.parseDouble(itemList.get(position).getCarbohydratesTotalG()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(itemListcopy);
        } else {
            for (ItemDb wp : itemListcopy) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    itemList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
