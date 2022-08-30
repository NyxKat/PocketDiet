package Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class NutritionExtra extends RealmObject implements Parcelable {

    private double bread;
    private double salad;
    private double sweets;

    public NutritionExtra(){

    }


    protected NutritionExtra(Parcel in) {
        Bundle bundle = in.readBundle();
        bread = bundle.getDouble("bread");
        salad = bundle.getDouble("salad");
        sweets = bundle.getDouble("sweets");
    }


    public NutritionExtra(double bread,double salad,double sweets){
        this.bread = bread;
        this.salad = salad;
        this.sweets = sweets;
    }

    public double getBread() {
        return bread;
    }

    public void setBread(double bread) {
        this.bread = bread;
    }

    public double getSalad() {
        return salad;
    }

    public void setSalad(double salad) {
        this.salad = salad;
    }

    public double getSweets() {
        return sweets;
    }

    public void setSweets(double sweets) {
        this.sweets = sweets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putDouble("bread", bread);
        bundle.putDouble("salad", salad);
        bundle.putDouble("sweets",sweets );


        dest.writeBundle(bundle);
    }




    public static final Parcelable.Creator<NutritionExtra> CREATOR = new Parcelable.Creator<NutritionExtra>() {
        @Override
        public NutritionExtra createFromParcel(Parcel in) {
            return new NutritionExtra(in);
        }

        @Override
        public NutritionExtra[] newArray(int size) {
            return new NutritionExtra[size];
        }
    };
}
