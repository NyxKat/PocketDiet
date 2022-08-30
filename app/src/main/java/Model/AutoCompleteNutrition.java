package Model;

import io.realm.RealmObject;

public class AutoCompleteNutrition extends RealmObject {
    private String food;

    public AutoCompleteNutrition(String food){
        this.food = food;
    }

    public AutoCompleteNutrition(){

    }

    public void setFood(String food){
        this.food = food;
    }

    public String getFood(){
        return food;
    }
}
