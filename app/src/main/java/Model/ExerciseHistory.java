package Model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class ExerciseHistory extends ExpandableGroup<Exercise> {

    public ArrayList<ArrayList<Exercise>> nutritionItems;

    public ExerciseHistory(String title, List<Exercise> items) {
        super(title, items);
    }


    public void setInnerItems( ArrayList<ArrayList<Exercise>> nutritionItems){
        this.nutritionItems = nutritionItems;
    }

    public  ArrayList<ArrayList<Exercise>> getInnerItems(){
        return nutritionItems;
    }

}
