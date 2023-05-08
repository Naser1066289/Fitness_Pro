package com.runora_dev.runoraf.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.runora_dev.runoraf.R;
import com.runora_dev.runoraf.Adapters.CustAdapter;
import com.runora_dev.runoraf.Model.ItemDb;
import com.runora_dev.runoraf.Webservice.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OfflineFragment extends Fragment implements SearchView.OnQueryTextListener {

    DatabaseHelper databaseHelper;
    List<ItemDb> itemList;
    RecyclerView datarec;
    View view;
    LottieAnimationView animationView;
    CustAdapter custAdapter;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_offline, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        searchView=view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        databaseHelper=new DatabaseHelper(getContext());
        animationView =view.findViewById(R.id.animation_view);
        datarec =view.findViewById(R.id.datarec);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        datarec.setLayoutManager(linearLayoutManager);

        animationView.addAnimatorUpdateListener(
                (animation) -> {

                });
        animationView.playAnimation();

        if (animationView.isAnimating()) {

        }
        Cursor data=databaseHelper.getAlldata();
        if(data.getCount()>0){

            itemList=new ArrayList<>();
            while (data.moveToNext()){
                //"sugar_g text,fiber_g text,
                // serving_size_g text,sodium_mg text,name text,
                // potassium_mg text,fat_saturated_g text,fat_total_g text,calories text,
                // cholesterol_mg text,protein_g text,carbohydrates_total_g text
                itemList.add(new ItemDb(data.getString(0), data.getString(1) ,
                        data.getString(2),data.getString(3),
                        data.getString(4), data.getString(5),
                        data.getString(6), data.getString(7),
                        data.getString(8), data.getString(9),
                        data.getString(10), data.getString(11),
                        data.getString(12)));
            }
            custAdapter=new CustAdapter(getContext(),itemList);
            datarec.setAdapter(custAdapter);
            animationView.setVisibility(View.GONE);
            datarec.setVisibility(View.VISIBLE);

        }else {
            animationView.setVisibility(View.VISIBLE);
            datarec.setVisibility(View.GONE);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        custAdapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        custAdapter.filter(newText);
        return true;
    }
}