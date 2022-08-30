package Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Nutrition extends RealmObject implements Parcelable {
    private String food;
    private double quantity;
    private String time;
    private String date;
    private int current_meal;
    private NutritionExtra nutritionExtra;


    private NutritionCalories nutritionCalories;
    private String type_of_meal;

    @PrimaryKey
    private int id;

    public Nutrition() {

    }

    protected Nutrition(Parcel in) {
        Bundle bundle = in.readBundle();
        in.readParcelable(NutritionExtra.class.getClassLoader());
        food = bundle.getString("food");
        time = bundle.getString("time");
        date = bundle.getString("date");
        quantity = bundle.getDouble("quantity");
        current_meal = bundle.getInt("current");
        id = bundle.getInt("id");

        double sweets = bundle.getDouble("sweets");
        double salad = bundle.getDouble("salad");
        double bread = bundle.getDouble("bread");

        nutritionExtra = new NutritionExtra(bread,salad,sweets);
        type_of_meal = bundle.getString("type");

        double carbs = bundle.getDouble("carbs");
        double fat = bundle.getDouble("fat");
        double protein = bundle.getDouble("protein");
        double calories = bundle.getDouble("calories");

        nutritionCalories = new NutritionCalories( carbs,  calories,  fat,  protein);

    }

    public Nutrition(String food, double quantity, String time, String date, int current_meal, NutritionExtra nutritionExtra,String type_of_meal,NutritionCalories nutritionCalories) {
        this.food = food;
        this.quantity = quantity;
        this.time = time;
        this.date = date;
        this.current_meal = current_meal;
        this.nutritionExtra = nutritionExtra;
        this.nutritionCalories = nutritionCalories;
        this.type_of_meal = type_of_meal;
    }

    public void setNutritionID(int id) {
        this.id = id;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCurrent_meal(int current_meal) {
        this.current_meal = current_meal;
    }

    public String getFood() {
        return food;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getCurrent_meal() {
        return current_meal;
    }

    public int getNutritionID() {
        return id;
    }

    public NutritionExtra getNutritionExtra() {
        return nutritionExtra;
    }

    public void setNutritionExtra(NutritionExtra nutritionExtra) {
        this.nutritionExtra = nutritionExtra;
    }

    public NutritionCalories getNutritionCalories() {
        return nutritionCalories;
    }

    public void setNutritionCalories(NutritionCalories nutritionCalories) {
        this.nutritionCalories = nutritionCalories;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        bundle.putString("food", food);
        bundle.putString("date", date);
        bundle.putString("time", time);
        bundle.putDouble("quantity", quantity);
        bundle.putInt("current", current_meal);
        bundle.putInt("id", id);

        bundle.putDouble("salad",nutritionExtra.getSalad());
        bundle.putDouble("bread",nutritionExtra.getBread());
        bundle.putDouble("sweets",nutritionExtra.getSweets());

        bundle.putDouble("calories",nutritionCalories.getCalories());
        bundle.putDouble("fat",nutritionCalories.getFat());
        bundle.putDouble("protein",nutritionCalories.getProtein());
        bundle.putDouble("carbs",nutritionCalories.getCarbs());

        //bundle.putParcelable("extra", nutritionExtra);
        bundle.putString("type",type_of_meal);


        dest.writeBundle(bundle);
    }




    public static final Parcelable.Creator<Nutrition> CREATOR = new Parcelable.Creator<Nutrition>() {
        @Override
        public Nutrition createFromParcel(Parcel in) {
            return new Nutrition(in);
        }

        @Override
        public Nutrition[] newArray(int size) {
            return new Nutrition[size];
        }
    };


    public String getType_of_meal() {
        return type_of_meal;
    }

    public void setType_of_meal(String type_of_meal) {
        this.type_of_meal = type_of_meal;
    }
}
