package com.runora_dev.runoraf.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.runora_dev.runoraf.R;
import com.runora_dev.runoraf.Adapters.CustAdapter;
import com.runora_dev.runoraf.Model.ItemDb;
import com.runora_dev.runoraf.Model.Responsedata;
import com.runora_dev.runoraf.Webservice.ApiClient;
import com.runora_dev.runoraf.Webservice.ApiInterface;
import com.runora_dev.runoraf.Webservice.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    String name;
    Integer sodiumMg = 0, potassiumMg = 0, cholesterolMg = 0;
    double sugarG = 0, fiberG = 0, servingSizeG = 0, fatSaturatedG = 0, fatTotalG = 0, calories = 0, proteinG = 0, carbohydratesTotalG = 0;
    View view;
    DatabaseHelper databaseHelper;
    List<ItemDb> itemList = new ArrayList<>();
    LottieAnimationView animationView;
    TextView nodata;
    RecyclerView datarec;
    SearchView searchView;
    CustAdapter custAdapter;
    ProgressDialog progressDialog;

    //Button btnDp,btnRep,btnFC,btnR;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading");
        databaseHelper = new DatabaseHelper(getContext());
        animationView = view.findViewById(R.id.animation_view);
        /*
        btnDp = view.findViewById(R.id.btndaily);
        btnFC = view.findViewById(R.id.btnfccalc);
        btnR = view.findViewById(R.id.btnreminder);
        btnRep = view.findViewById(R.id.btndailyrep);
        */
        nodata = view.findViewById(R.id.nodata);
        datarec = view.findViewById(R.id.datarec);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        datarec.setLayoutManager(linearLayoutManager);

        animationView.addAnimatorUpdateListener(
                (animation) -> {

                });
        animationView.playAnimation();

        if (animationView.isAnimating()) {

        }
        searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        Cursor data = databaseHelper.getAlldata();
        if (data.getCount() > 0) {
            itemList.clear();
            while (data.moveToNext()) {
                //"sugar_g text,fiber_g text,
                // serving_size_g text,sodium_mg text,name text,
                // potassium_mg text,fat_saturated_g text,fat_total_g text,calories text,
                // cholesterol_mg text,protein_g text,carbohydrates_total_g text
                itemList.add(new ItemDb(data.getString(0), data.getString(1),
                        data.getString(2), data.getString(3),
                        data.getString(4), data.getString(5),
                        data.getString(6), data.getString(7),
                        data.getString(8), data.getString(9),
                        data.getString(10), data.getString(11),
                        data.getString(12)));
            }
            custAdapter = new CustAdapter(getContext(), itemList);
            datarec.setAdapter(custAdapter);
            animationView.setVisibility(View.GONE);
            nodata.setVisibility(View.GONE);
            datarec.setVisibility(View.VISIBLE);

        } else {
            animationView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.VISIBLE);
            datarec.setVisibility(View.GONE);

        }
        /*
        btnDp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent obj = new Intent(getActivity().getApplicationContext(),DailyActivity.class);
                        startActivity(obj);
                }
        });
        btnFC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent obj = new Intent(getActivity().getApplicationContext(),ApiActivity.class);
                        startActivity(obj);
                }
        });
        btnRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent obj = new Intent(getActivity().getApplicationContext(),DailyReportActivity.class);
                        startActivity(obj);
                }
        });
        btnR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent obj = new Intent(getActivity().getApplicationContext(),RemindersActivity.class);
                        startActivity(obj);
                }
        });
         */
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressDialog.show();
        ApiInterface apiInterface = new ApiClient().getClint().create(ApiInterface.class);
        Call<Responsedata> Item = apiInterface.getItem(query, "KIXegjpFzaguzHSadAjKEw==MJVLTUaq52lRHu1g");
        Item.enqueue(new Callback<Responsedata>() {
            @Override
            public void onResponse(Call<Responsedata> call, Response<Responsedata> response) {

                List<com.runora_dev.runoraf.Model.Item> itemList = response.body().getItems();
                // Log.e("data:",itemList.get(0).getProteinG().toString());
                for (int i = 0; i < itemList.size(); i++) {
                    name = query;
                    //int
                    sodiumMg += itemList.get(i).getSodiumMg();
                    potassiumMg += itemList.get(i).getPotassiumMg();
                    cholesterolMg += itemList.get(i).getCholesterolMg();
                    //   double
                    sugarG += itemList.get(i).getSugarG();
                    fiberG += itemList.get(i).getFiberG();
                    servingSizeG += itemList.get(i).getServingSizeG();
                    fatSaturatedG += itemList.get(i).getFatSaturatedG();
                    fatTotalG += itemList.get(i).getFatTotalG();
                    calories += itemList.get(i).getCalories();
                    proteinG += itemList.get(i).getProteinG();
                    carbohydratesTotalG += itemList.get(i).getCarbohydratesTotalG();

                }
                if (itemList.size() > 0) {

                    Intent intent = new Intent(getContext(), DetailActicity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("sodiumMg", sodiumMg);
                    intent.putExtra("potassiumMg", potassiumMg);
                    intent.putExtra("cholesterolMg", cholesterolMg);
                    intent.putExtra("sugarG", sugarG);
                    intent.putExtra("fiberG", fiberG);
                    intent.putExtra("servingSizeG", servingSizeG);
                    intent.putExtra("fatSaturatedG", fatSaturatedG);
                    intent.putExtra("fatTotalG", fatTotalG);
                    intent.putExtra("calories", calories);
                    intent.putExtra("proteinG", proteinG);
                    intent.putExtra("carbohydratesTotalG", carbohydratesTotalG);
                    progressDialog.dismiss();
                    getContext().startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Sorry no data found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Responsedata> call, Throwable t) {
                Log.e("error:", t.toString());
                progressDialog.dismiss();
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (itemList.size() > 0) {
            custAdapter.filter(newText);
        }
        return true;
    }
}