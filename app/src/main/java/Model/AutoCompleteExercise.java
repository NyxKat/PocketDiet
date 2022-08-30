package Model;

import io.realm.RealmObject;

public class AutoCompleteExercise extends RealmObject {
    private String exercice;

    public AutoCompleteExercise(String exercice){
        this.exercice = exercice;
    }

    public AutoCompleteExercise(){

    }

    public void setExercice(String exercice){
        this.exercice = exercice;
    }

    public String getExercice(){
        return exercice;
    }
}
