package com.runora_dev.runoraf.Webservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper  extends SQLiteOpenHelper {
    public static  final String data_basename="food.db";
    public static  final String food_table="Fooddata";
    public static  final String id="id";
    public static  final String sugar_g="sugar_g";

    public static  final String fiber_g="fiber_g";
    public static  final String serving_size_g="serving_size_g";
    public static  final String sodium_mg="sodium_mg";
    public static  final String name="name";
    public static  final String potassium_mg="potassium_mg";
    public static  final String fat_saturated_g="fat_saturated_g";
    public static  final String fat_total_g="fat_total_g";
    public static  final String calories="calories";
    public static  final String cholesterol_mg="cholesterol_mg";
    public static  final String protein_g="protein_g";
    public static  final String carbohydrates_total_g="carbohydrates_total_g";


    public DatabaseHelper(@Nullable Context context) {
        super(context, data_basename, null, 1);
        SQLiteDatabase db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table "+food_table+"(id integer primary key autoincrement," +"sugar_g text,fiber_g text,serving_size_g text,sodium_mg text,name text,potassium_mg text,fat_saturated_g text,fat_total_g text,calories text,cholesterol_mg text,protein_g text,carbohydrates_total_g text )");
        db.execSQL("create table dailyplanner(id integer primary key autoincrement,bitem text,bc real, litem text,lc real, ditem text,dc real, ddate text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("drop table if exists "+food_table);
        db.execSQL("drop table if exists dailyplanner");
        onCreate(db);
    }
    public boolean addfood(String sugarG, String fiberG, String servingSizeG, String sodiumMg, String name1, String potassiumMg, String fatSaturatedG, String fatTotalG, String calories1, String cholesterolMg, String proteinG,String carbohydratesTotalG) {
        ContentValues c=new ContentValues();
        c.put(sugar_g,sugarG);
        c.put(fiber_g,fiberG);
        c.put(serving_size_g,servingSizeG);
        c.put(sodium_mg,sodiumMg);
        c.put(name,name1);
        c.put(potassium_mg,potassiumMg);
        c.put(fat_saturated_g,fatSaturatedG);
        c.put(fat_total_g,fatTotalG);
        c.put(calories,calories1);
        c.put(cholesterol_mg,cholesterolMg);
        c.put(protein_g,proteinG);
        c.put(carbohydrates_total_g,carbohydratesTotalG);
        SQLiteDatabase db=getWritableDatabase();
        long reslt= db.insert(food_table,null,c);
        if(reslt==-1){
            return false;
        }
        else {
            return  true;
        }


    }
    public boolean addDailyFood(String bf,float bc, String lf, float lc, String df,float dc,String dt) {
        ContentValues c=new ContentValues();
        c.put("bitem",bf);
        c.put("bc",bc);
        c.put("litem",lf);
        c.put("lc",lc);
        c.put("ditem",df);
        c.put("dc",dc);
        c.put("ddate",dt);
        SQLiteDatabase db=getWritableDatabase();
        long reslt= db.insert("dailyplanner",null,c);
        if(reslt==-1){
            return false;
        }
        else {
            return  true;
        }
    }
    public Cursor getAllDailydata()
    {
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from dailyplanner",null);
        return res;
    }


    public Cursor getAlldata()
    {
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ food_table,null);
        return res;
    }


    public Cursor getDetails(String id)
    {
        SQLiteDatabase db=getWritableDatabase();
        Cursor res= db.rawQuery("SELECT * FROM Fooddata WHERE fid = '"+id+"'", null);
        return res;
    }
    public void Delete(String name){
        SQLiteDatabase db = getWritableDatabase();
        //db.rawQuery("delete from Fooddata where name = '"+name+"'", null);
        db.delete("Fooddata", "name='"+name+"'", null);

        //finish();
    }


}
