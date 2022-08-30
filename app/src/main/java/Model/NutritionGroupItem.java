package Model;

import java.util.ArrayList;

public class NutritionGroupItem {
    private String month;
    private int year;
    private ArrayList<Nutrition> childItems;
    private ArrayList<Nutrition> distict_childs;
    private ArrayList<ArrayList<Nutrition>> group_of_childs;

    public NutritionGroupItem(String month, int year){
        this.month=month;
        this.year=year;
        childItems=new ArrayList<>();
    }

    public void addChild(Nutrition childItem){
        childItems.add(childItem);
    }

    public String getMonth(){
        return month;
    }

    public ArrayList<Nutrition> getChildItems(){
        return childItems;
    }


    public int getYear(){
        return year;
    }

    public ArrayList<Nutrition> getDistict_childs(){
        return distict_childs;
    }

    public ArrayList<ArrayList<Nutrition>> getGroup_of_childs(){
        return group_of_childs;
    }

    public void setup(){

        distict_childs = new ArrayList<>();
        group_of_childs = new ArrayList<>();
        int counter = 0;

        for(int i=0; i<childItems.size(); i++){

            if(distict_childs.isEmpty()){
                distict_childs.add(childItems.get(i));
                ArrayList<Nutrition> nutritionItems = new ArrayList<>();
                nutritionItems.add(childItems.get(i));
                group_of_childs.add(nutritionItems);
                continue;
            }

            boolean flag = false;
            for(int j=0; j<distict_childs.size(); j++){

                if(distict_childs.get(j).getDate().equals(childItems.get(i).getDate())){
                    group_of_childs.get(counter).add(childItems.get(i));
                    flag = true;
                    break;
                }
            }

            if(!flag){
                counter++;
                distict_childs.add(childItems.get(i));
                ArrayList<Nutrition> nutritionItems = new ArrayList<>();
                nutritionItems.add(childItems.get(i));
                group_of_childs.add(nutritionItems);
            }

        }
    }

}
