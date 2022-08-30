package Model;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/*
RealmDatabase class is simulating the database of the PocketDiet. This database is a realm database which is a local database.
Nutrition and Exercise are the main realm objects that are used to store and retrieve data from the database, these realm objects relates to the actual tables that are used on the database.

In this class all the methods that handles the data of the application is implemented. Basic CRUD operations are supported such as creating a new nutrition realm object, deleting a existing nutrition realm object
and update an existing nutrition realm object. The same applies for the Exercise realm objects. There are many filtering methods implemented also.
 */
public class RealmDatabase {
    private Realm realm;

    public RealmDatabase(Context context) {
        Realm.init(context);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .name("PocketDiet.realm")
                .build();

        realm = Realm.getInstance(realmConfiguration);
    }


    public void insertAutoCompleteExercise(final AutoCompleteExercise exercise) {
        RealmResults<AutoCompleteExercise> autoCompleteExercises = realm.where(AutoCompleteExercise.class).equalTo("exercice", exercise.getExercice()).findAll();
        if (autoCompleteExercises.isEmpty()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(exercise);
                }
            });
        }
    }

    public void insertAutoCompleteNutrition(final AutoCompleteNutrition autoCompleteNutrition) {
        RealmResults<AutoCompleteNutrition> autoCompleteNutritions = realm.where(AutoCompleteNutrition.class).equalTo("food", autoCompleteNutrition.getFood()).findAll();
        if (autoCompleteNutritions.isEmpty()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(autoCompleteNutrition);
                }
            });
        }
    }


    public void insertOrUpdateNutrition(final Nutrition nutrition) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(nutrition);
            }
        });

    }

    public void insertOrUpdateExercise(final Exercise exercise) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(exercise);
            }
        });
    }

    public void getAllNutritions() {

        RealmResults<Nutrition> items = realm.where(Nutrition.class).findAll();


        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Nutrition nutrition = items.get(i);
                Log.e("MealTime", nutrition.getTime());
                Log.e("MealDate", nutrition.getDate());
                Log.e("Meal", nutrition.getFood());
                Log.e("MealQuantity", nutrition.getQuantity() + "");
                Log.e("MealCurrentMEal", nutrition.getCurrent_meal() + "");
                Log.e("MealID", nutrition.getNutritionID() + "");
            }
        }

    }

    public void getAllExercise() {

        RealmResults<Exercise> items = realm.where(Exercise.class).findAll();


        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Exercise exercise = items.get(i);
                Log.e("ExerciseTime", exercise.getTime());
                Log.e("ExerciseDate", exercise.getDate());
                Log.e("Exercise", exercise.getExercise());
                Log.e("WorkTime", exercise.getWorkTime() + "");
                //Log.e("Period of day", exercise.getPeriodOfDay());
                Log.e("ExerciseID", exercise.getId() + "");
            }
        }

    }


    public int getNutritionNextKey() {
        if (realm.where(Nutrition.class).findAll().isEmpty()) return 1;


        return realm.where(Nutrition.class).max("id").intValue() + 1;
    }

    public int getExerciseNextKey() {
        if (realm.where(Exercise.class).findAll().isEmpty()) return 1;


        return realm.where(Exercise.class).max("id").intValue() + 1;
    }


    public Nutrition getTheMostRecentNutrition() {
        Nutrition nutrition = null;

        RealmResults<Nutrition> items = realm.where(Nutrition.class).findAll();
        if (!items.isEmpty()) {
            String[] fields = new String[3];
            Sort[] sorted = new Sort[3];
            fields[0] = "date";
            fields[1] = "time";
            fields[2] = "id";

            sorted[0] = Sort.DESCENDING;
            sorted[1] = Sort.DESCENDING;
            sorted[2] = Sort.DESCENDING;


            RealmResults<Nutrition> sortedItems = items.sort(fields, sorted);
            nutrition = sortedItems.get(0);

        }

        return nutrition;

    }

    public Exercise getTheMostRecentExercise() {
        Exercise exercise = null;

        RealmResults<Exercise> items = realm.where(Exercise.class).findAll();
        if (!items.isEmpty()) {
            String[] fields = new String[3];
            Sort[] sorted = new Sort[3];
            fields[0] = "date";
            fields[1] = "time";
            fields[2] = "id";


            sorted[0] = Sort.DESCENDING;
            sorted[1] = Sort.DESCENDING;
            sorted[2] = Sort.DESCENDING;


            RealmResults<Exercise> sortedItems = items.sort(fields, sorted);
            exercise = sortedItems.get(0);

        }

        return exercise;
    }


    public RealmResults<Nutrition> getAllNutritionFromNewestToOldest() {

        RealmResults<Nutrition> items = realm.where(Nutrition.class).findAll();
        String[] fields = new String[2];
        Sort[] sorted = new Sort[2];
        fields[0] = "date";
        fields[1] = "id";

        sorted[0] = Sort.DESCENDING;
        sorted[1] = Sort.ASCENDING;

        RealmResults<Nutrition> sortedItems = items.sort(fields, sorted);


        return sortedItems;
    }

    public RealmResults<Nutrition> getNutritionByFood(String food) {
        RealmResults<Nutrition> items = realm.where(Nutrition.class).equalTo("food", food).findAll();

        if (!items.isEmpty()) {
            String[] fields = new String[2];
            Sort[] sorted = new Sort[2];
            fields[0] = "date";
            fields[1] = "id";

            sorted[0] = Sort.DESCENDING;
            sorted[1] = Sort.ASCENDING;

            RealmResults<Nutrition> sortedItems = items.sort(fields, sorted);

            return sortedItems;
        }


        return items;
    }

    public RealmResults<Nutrition> getNutritionByDate(String date) {
        RealmResults<Nutrition> items = realm.where(Nutrition.class).equalTo("date", date).findAll();
        return items;
    }


    public RealmResults<Exercise> getAllExerciseFromNewestToOldest() {

        RealmResults<Exercise> items = realm.where(Exercise.class).findAll();
        String[] fields = new String[2];
        Sort[] sorted = new Sort[2];
        fields[0] = "date";
        fields[1] = "id";

        sorted[0] = Sort.DESCENDING;
        sorted[1] = Sort.ASCENDING;

        RealmResults<Exercise> sortedItems = items.sort(fields, sorted);


        return sortedItems;
    }

    public RealmResults<Exercise> getExerciseByName(String exercise) {
        RealmResults<Exercise> items = realm.where(Exercise.class).equalTo("exercise", exercise).findAll();
        String[] fields = new String[2];
        Sort[] sorted = new Sort[2];
        fields[0] = "date";
        fields[1] = "id";

        sorted[0] = Sort.DESCENDING;
        sorted[1] = Sort.ASCENDING;

        RealmResults<Exercise> sortedItems = items.sort(fields, sorted);


        return sortedItems;
    }

    public RealmResults<Exercise> getExerciseByDate(String date) {
        RealmResults<Exercise> items = realm.where(Exercise.class).equalTo("date", date).findAll();
        return items;
    }


    public Exercise getExerciseBasedOnID(int id) {
        Exercise exercise = null;

        RealmResults<Exercise> results = realm.where(Exercise.class).equalTo("id", id).findAll();
        if (!results.isEmpty()) {
            exercise = results.get(0);
        }

        return exercise;
    }

    public int getCurrentMealNumber(String date) {
        int currentNumberOfMeal = 1;

        RealmResults<Nutrition> results = realm.where(Nutrition.class).equalTo("date", date).findAll();

        if (!results.isEmpty()) {
            currentNumberOfMeal = results.size() + 1;
        }

        return currentNumberOfMeal;
    }


    public ArrayList<String> getAutoCompleteNutritionBasedOnFirst2Characters(String startswith) {
        ArrayList<String> foods = new ArrayList<>();

        RealmResults<AutoCompleteNutrition> results = realm.where(AutoCompleteNutrition.class).beginsWith("food", startswith).findAll();

        if (!results.isEmpty()) {
            for (int i = 0; i < results.size(); i++) {
                foods.add(results.get(i).getFood());
            }
        }

        return foods;
    }

    public ArrayList<String> getAutoCompleteExerciseBasedOnFirst2Characters(String startswith) {
        ArrayList<String> exercises = new ArrayList<>();

        RealmResults<AutoCompleteExercise> results = realm.where(AutoCompleteExercise.class).beginsWith("exercice", startswith).findAll();

        if (!results.isEmpty()) {
            for (int i = 0; i < results.size(); i++) {
                exercises.add(results.get(i).getExercice());
            }
        }

        return exercises;
    }


    public void deleteExercise(int id) {
        Exercise exercise = realm.where(Exercise.class).equalTo("id", id).findFirst();

        if (exercise != null) {
            realm.beginTransaction();
            exercise.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public void deleteNutrition(int id) {
        Nutrition nutrition = realm.where(Nutrition.class).equalTo("id", id).findFirst();

        if (nutrition != null) {
            realm.beginTransaction();
            nutrition.deleteFromRealm();
            realm.commitTransaction();
        }
    }

}