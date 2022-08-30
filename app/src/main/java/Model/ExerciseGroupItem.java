package Model;

import java.util.ArrayList;

public class ExerciseGroupItem {
    private String month;
    private int year;
    private ArrayList<Exercise> childItems;
    private ArrayList<Exercise> distict_childs;
    private ArrayList<ArrayList<Exercise>> group_of_childs;

    public ExerciseGroupItem(String month, int year) {
        this.month = month;
        this.year = year;
        childItems = new ArrayList<>();
    }

    public void addChild(Exercise childItem) {
        childItems.add(childItem);
    }

    public String getMonth() {
        return month;
    }

    public ArrayList<Exercise> getChildItems() {
        return childItems;
    }


    public int getYear() {
        return year;
    }

    public ArrayList<Exercise> getDistict_childs() {
        return distict_childs;
    }

    public ArrayList<ArrayList<Exercise>> getGroup_of_childs() {
        return group_of_childs;
    }

    public void setup() {

        distict_childs = new ArrayList<>();
        group_of_childs = new ArrayList<>();
        int counter = 0;

        for (int i = 0; i < childItems.size(); i++) {

            if (distict_childs.isEmpty()) {
                distict_childs.add(childItems.get(i));
                ArrayList<Exercise> exerciseItems = new ArrayList<>();
                exerciseItems.add(childItems.get(i));
                group_of_childs.add(exerciseItems);
                continue;
            }

            boolean flag = false;
            for (int j = 0; j < distict_childs.size(); j++) {

                if (distict_childs.get(j).getDate().equals(childItems.get(i).getDate())) {
                    group_of_childs.get(counter).add(childItems.get(i));
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                counter++;
                distict_childs.add(childItems.get(i));
                ArrayList<Exercise> exerciseItems = new ArrayList<>();
                exerciseItems.add(childItems.get(i));
                group_of_childs.add(exerciseItems);
            }

        }
    }

}
