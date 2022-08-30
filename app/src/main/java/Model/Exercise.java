package Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Exercise extends RealmObject implements Parcelable {
    private String exercise;
    private String date;
    private String time;
    //    private String periodOfDay;
    private double workTime;


    @PrimaryKey
    private int id;


    protected Exercise(Parcel in) {
        Bundle bundle = in.readBundle();
        exercise = bundle.getString("exercise");
        time = bundle.getString("time");
        date = bundle.getString("date");
        workTime = bundle.getDouble("workTime");
        //periodOfDay = bundle.getString("period");
        id = bundle.getInt("id");
    }

    public Exercise() {
        exercise = "";
        time = "";
    }

    public Exercise(String exercise, String date, String time, double workTime) {

        this.exercise = exercise;
        this.date = date;
        this.workTime = workTime;
        // this.periodOfDay = periodOfDay;
        this.time = time;

    }

    public void setId(int id){
        this.id = id;
    }


    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setWorkTime(double workTime) {
        this.workTime = workTime;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExercise() {
        return exercise;
    }

    public String getDate() {
        return date;
    }


    public double getWorkTime() {
        return workTime;
    }

    public String getTime() {
        return time;
    }

    public int getId(){
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("exercise", exercise);
        bundle.putString("date", date);
        bundle.putString("time", time);
        bundle.putDouble("workTime", workTime);
        bundle.putInt("id", id);

        dest.writeBundle(bundle);
    }


    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
}
