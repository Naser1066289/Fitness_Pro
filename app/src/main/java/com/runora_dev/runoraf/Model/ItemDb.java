package com.runora_dev.runoraf.Model;

public class ItemDb {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSugarG() {
        return sugarG;
    }

    public void setSugarG(String sugarG) {
        this.sugarG = sugarG;
    }

    public String getFiberG() {
        return fiberG;
    }

    public void setFiberG(String fiberG) {
        this.fiberG = fiberG;
    }

    public String getServingSizeG() {
        return servingSizeG;
    }

    public void setServingSizeG(String servingSizeG) {
        this.servingSizeG = servingSizeG;
    }

    public String getSodiumMg() {
        return sodiumMg;
    }

    public void setSodiumMg(String sodiumMg) {
        this.sodiumMg = sodiumMg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPotassiumMg() {
        return potassiumMg;
    }

    public void setPotassiumMg(String potassiumMg) {
        this.potassiumMg = potassiumMg;
    }

    public String getFatSaturatedG() {
        return fatSaturatedG;
    }

    public void setFatSaturatedG(String fatSaturatedG) {
        this.fatSaturatedG = fatSaturatedG;
    }

    public String getFatTotalG() {
        return fatTotalG;
    }

    public void setFatTotalG(String fatTotalG) {
        this.fatTotalG = fatTotalG;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCholesterolMg() {
        return cholesterolMg;
    }

    public void setCholesterolMg(String cholesterolMg) {
        this.cholesterolMg = cholesterolMg;
    }

    public String getProteinG() {
        return proteinG;
    }

    public void setProteinG(String proteinG) {
        this.proteinG = proteinG;
    }

    public String getCarbohydratesTotalG() {
        return carbohydratesTotalG;
    }

    public void setCarbohydratesTotalG(String carbohydratesTotalG) {
        this.carbohydratesTotalG = carbohydratesTotalG;
    }

    public ItemDb(String id, String sugarG, String fiberG, String servingSizeG, String sodiumMg, String name, String potassiumMg, String fatSaturatedG, String fatTotalG, String calories, String cholesterolMg, String proteinG, String carbohydratesTotalG) {
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

    String id,sugarG,fiberG,servingSizeG,sodiumMg,name,potassiumMg,fatSaturatedG,fatTotalG,calories,cholesterolMg,proteinG, carbohydratesTotalG;

}
