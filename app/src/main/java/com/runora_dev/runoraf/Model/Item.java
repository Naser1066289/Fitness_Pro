package com.runora_dev.runoraf.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    String id;
    @SerializedName("sugar_g")
    @Expose
    private Double sugarG;
    @SerializedName("fiber_g")
    @Expose
    private Double fiberG;
    @SerializedName("serving_size_g")
    @Expose
    private Double servingSizeG;
    @SerializedName("sodium_mg")
    @Expose
    private Integer sodiumMg;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("potassium_mg")
    @Expose
    private Integer potassiumMg;
    @SerializedName("fat_saturated_g")
    @Expose
    private Double fatSaturatedG;
    @SerializedName("fat_total_g")
    @Expose
    private Double fatTotalG;
    @SerializedName("calories")
    @Expose
    private Double calories;
    @SerializedName("cholesterol_mg")
    @Expose
    private Integer cholesterolMg;
    @SerializedName("protein_g")
    @Expose
    private Double proteinG;
    @SerializedName("carbohydrates_total_g")
    @Expose
    private Double carbohydratesTotalG;
    public Item(String id, Double sugarG, Double fiberG, Double servingSizeG, Integer sodiumMg, String name, Integer potassiumMg, Double fatSaturatedG, Double fatTotalG, Double calories, Integer cholesterolMg, Double proteinG, Double carbohydratesTotalG) {
        this.id = id;
        this.sugarG = sugarG;
        this.fiberG = fiberG;
        this.servingSizeG = servingSizeG;
        this.sodiumMg = sodiumMg;
        this.name = name;
        this.potassiumMg = potassiumMg;
        this.fatSaturatedG = fatSaturatedG;
        this.fatTotalG = fatTotalG;
        this.calories = calories;
        this.cholesterolMg = cholesterolMg;
        this.proteinG = proteinG;
        this.carbohydratesTotalG = carbohydratesTotalG;
    }

    public Item() {
    }

    public Item(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10, String s11, String s12, String s13, String s14, String s15, String s16, String s17, String s18, String s19, String s20) {
    }
    public Item(Double sugarG, Double fiberG, Double servingSizeG, Integer sodiumMg, String name, Integer potassiumMg, Double fatSaturatedG, Double fatTotalG, Double calories, Integer cholesterolMg, Double proteinG, Double carbohydratesTotalG) {
        this.sugarG = sugarG;
        this.fiberG = fiberG;
        this.servingSizeG = servingSizeG;
        this.sodiumMg = sodiumMg;
        this.name = name;
        this.potassiumMg = potassiumMg;
        this.fatSaturatedG = fatSaturatedG;
        this.fatTotalG = fatTotalG;
        this.calories = calories;
        this.cholesterolMg = cholesterolMg;
        this.proteinG = proteinG;
        this.carbohydratesTotalG = carbohydratesTotalG;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getSugarG() {
        return sugarG;
    }

    public void setSugarG(Double sugarG) {
        this.sugarG = sugarG;
    }

    public Double getFiberG() {
        return fiberG;
    }

    public void setFiberG(Double fiberG) {
        this.fiberG = fiberG;
    }

    public Double getServingSizeG() {
        return servingSizeG;
    }

    public void setServingSizeG(Double servingSizeG) {
        this.servingSizeG = servingSizeG;
    }

    public Integer getSodiumMg() {
        return sodiumMg;
    }

    public void setSodiumMg(Integer sodiumMg) {
        this.sodiumMg = sodiumMg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPotassiumMg() {
        return potassiumMg;
    }

    public void setPotassiumMg(Integer potassiumMg) {
        this.potassiumMg = potassiumMg;
    }

    public Double getFatSaturatedG() {
        return fatSaturatedG;
    }

    public void setFatSaturatedG(Double fatSaturatedG) {
        this.fatSaturatedG = fatSaturatedG;
    }

    public Double getFatTotalG() {
        return fatTotalG;
    }

    public void setFatTotalG(Double fatTotalG) {
        this.fatTotalG = fatTotalG;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Integer getCholesterolMg() {
        return cholesterolMg;
    }

    public void setCholesterolMg(Integer cholesterolMg) {
        this.cholesterolMg = cholesterolMg;
    }

    public Double getProteinG() {
        return proteinG;
    }

    public void setProteinG(Double proteinG) {
        this.proteinG = proteinG;
    }

    public Double getCarbohydratesTotalG() {
        return carbohydratesTotalG;
    }

    public void setCarbohydratesTotalG(Double carbohydratesTotalG) {
        this.carbohydratesTotalG = carbohydratesTotalG;
    }
}
