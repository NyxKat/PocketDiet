package Model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class NutritionHistory extends ExpandableGroup<Nutrition> {

    public ArrayList<ArrayList<Nutrition>> nutritionItems;

    public NutritionHistory(String title, List<Nutrition> items) {
        super(title, items);
    }


    public void setInnerItems( ArrayList<ArrayList<Nutrition>> nutritionItems){
        this.nutritionItems = nutritionItems;
    }

    public  ArrayList<ArrayList<Nutrition>> getInnerItems(){
        return nutritionItems;
    }

}
