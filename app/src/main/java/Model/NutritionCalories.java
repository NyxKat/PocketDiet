package Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class NutritionCalories extends RealmObject {
    private double carbs;
    private double calories;
    private double fat;
    private double protein;

    public NutritionCalories(){

    }


    public NutritionCalories(double carbs, double calories, double fat, double protein) {
        this.carbs = carbs;
        this.calories = calories;
        this.fat = fat;
        this.protein = protein;
    }


    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

}
