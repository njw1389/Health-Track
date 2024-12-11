package App.controller;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;


import App.model.DailyLog;
import App.model.Exercise;
import App.model.ExerciseEntry;
import App.model.Food;
import App.model.FoodEntry;
import App.view.UserInterface;
import App.model.BasicFood;
import App.model.Recipe;

public class LogController {

    // Log model;
    UserInterface view;
    public FoodController foodController;
    public ExerciseController exerciseController;

    private HashMap<String, DailyLog> dateToLog = new HashMap<String, DailyLog>();
    private DailyLog currentDailyLog;

    private static String log = "src/main/resources/log.csv";

    private List<List<String>> records = new ArrayList<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/m/dd");

    public LogController(UserInterface view, FoodController foodController, ExerciseController exerciseController) {
        // this.model = model;
        this.view = view;
        this.foodController = foodController;
        this.exerciseController = exerciseController;

        readDB();
    }

    private void readDB() {
        List<BasicFood> bfList = foodController.getBasicFoodList();
        List<Recipe> rList = foodController.getRecipeList();
        List<Exercise> exercises = exerciseController.getExerciseList();

        try {

            BufferedReader br = new BufferedReader(new FileReader(log));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));

            }

            br.close();

            currentDailyLog = new DailyLog();

            for (int i = 0; i < records.size(); i++) {
                String stringDate = records.get(i).get(0) + "/" + records.get(i).get(1) + "/" + records.get(i).get(2);
                DailyLog current;
                if (dateToLog.containsKey(stringDate)) {
                    current = dateToLog.get(stringDate);
                } else {
                    current = new DailyLog();

                    dateToLog.put(stringDate, current);
                }
                if (current.getDate() == null) {
                    Date newDate = formatter.parse(stringDate);

                    current.setDate(newDate);
                }

                // Filters through to get the latest daily log --might remove
                if (currentDailyLog.getDate() != null && current.getDate().before(currentDailyLog.getDate())) {
                    currentDailyLog = current;
                }

                if (records.get(i).get(3).equals("w")) {
                    current.setWeight(Double.parseDouble(records.get(i).get(4)));

                } else if (records.get(i).get(3).equals("c")) {
                    current.setCaloriesLimit(Double.parseDouble(records.get(i).get(4)));
                } else if (records.get(i).get(3).equals("f")) {
                    Food food = null;
                    for (BasicFood b : bfList) {
                        if (b.getName().equals(records.get(i).get(4))) {
                            food = b;
                        }
                    }

                    for (Recipe r : rList) {
                        if (r.getName().equals(records.get(i).get(4))) {
                            food = r;
                        }
                    }

                    FoodEntry entry = new FoodEntry(food, Double.parseDouble(records.get(i).get(5)));
                    current.addFoodEntry(entry);
                } else if (records.get(i).get(3).equals("e")) {
                    Exercise eObj = null;

                    for (Exercise k : exercises) {
                        if (records.get(i).get(4).equals(k.getName())) {
                            eObj = k;
                        }
                    }

                    ExerciseEntry newExerciseEntry = new ExerciseEntry(eObj, Double.parseDouble(records.get(i).get(5)));
                    current.addExerciseEntry(newExerciseEntry);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    public void updateDB() {
        updateRecords();

        try {
            FileWriter fw = new FileWriter(log);
            BufferedWriter writer = new BufferedWriter(fw);

            for (int i = 0; i < records.size(); i++) {
                // System.out.println(records.get(i).toString());
                for (int j = 0; j < records.get(i).size(); j++) {
                    writer.write(records.get(i).get(j).toString());
                    writer.write(",");
                }
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void updateRecords() {
        records = new ArrayList<>();

        for (HashMap.Entry<String, DailyLog> i : dateToLog.entrySet()) {

            String dateString = i.getKey();
            String[] splitString = dateString.split("/");

            DailyLog dl = i.getValue();
            List<String> calRow = new ArrayList<>();
            List<String> weightRow = new ArrayList<>();

            String calsToAdd = String.valueOf(dl.getCaloriesLimit());
            String weightToAdd = String.valueOf(dl.getWeight());

            if (!calsToAdd.equals("0.0")) {
                calRow.add(splitString[0]);
                calRow.add(splitString[1]);
                calRow.add(splitString[2]);
                calRow.add("c");
                calRow.add(calsToAdd);
                records.add(calRow);

            }
            if (!weightToAdd.equals("0.0")) {
                weightRow.add(splitString[0]);
                weightRow.add(splitString[1]);
                weightRow.add(splitString[2]);
                weightRow.add("w");
                weightRow.add(weightToAdd);
                records.add(weightRow);
            }

            List<FoodEntry> foodEntryList = dl.getFoodEntries();

            for (int j = 0; j < dl.getFoodEntries().size(); j++) {
                List<String> row = new ArrayList<>();
                FoodEntry fe = foodEntryList.get(j);

                row.add(splitString[0]);
                row.add(splitString[1]);
                row.add(splitString[2]);

                row.add("f");

                row.add(fe.getFood().getName());
                row.add(String.valueOf(fe.getServings()));

                records.add(row);
            }
            List<ExerciseEntry> exerciseEntryList = dl.getExerciseEntries();
            for (int k = 0; k < exerciseEntryList.size(); k++) {
                List<String> row = new ArrayList<>();

                ExerciseEntry ee = exerciseEntryList.get(k);

                row.add(splitString[0]);
                row.add(splitString[1]);
                row.add(splitString[2]);

                row.add("e");

                row.add(ee.getExercise().getName());
                row.add(String.valueOf(ee.getMinutes()));

                records.add(row);
            }
        }

    }

    public void addExerciseEntry(Date date, String name, Double minutes) {
        List<Exercise> exercises = exerciseController.getExerciseList();
        Exercise exercise = null;

        for (Exercise e : exercises) {
            if (e.getName().equals(name)) {
                exercise = e;
            }

        }

        if (exercise == null) {
            return;
        }

        ExerciseEntry exerciseEntryTmp = new ExerciseEntry(exercise, minutes);
        getDailyLog(date).addExerciseEntry(exerciseEntryTmp);
    }

    public Double calculateCaloriesBurned(DailyLog dl) {
        List<ExerciseEntry> exerciseEntryList = dl.getExerciseEntries();
        Double totalCalBurned = 0.0;
        for (ExerciseEntry exerciseEntry : exerciseEntryList) {
            String exerciseName = exerciseEntry.getExercise().getName();
            Double weight = dl.getWeight();
            Double minutes = exerciseEntry.getMinutes();
            totalCalBurned += exerciseController.calculateCaloriesExpended(exerciseName, weight, minutes);
        }
        return totalCalBurned;
    }

    public Date getMostRecentDate(){
        Date temp = null;
        try{
            List<Date> sorted = new ArrayList<>();
            for(String i : dateToLog.keySet()){
                try{
                    temp = formatter.parse(i);
                    sorted.add(temp);
                }
                catch(Exception e){
                    System.out.println("Error: " + e);
                }
            }

            Collections.sort(sorted);
            return sorted.get((sorted.size() - 1));
        }
        catch(Exception e){
            System.out.println("Error: invalid date format");
        }

        return null;
        
    }

    public List<FoodEntry> getFoodEntriesFromDailyLog(DailyLog dl) {
        return dl.getFoodEntries();
    }

    public String getDailyLogChartInfo(DailyLog dl) {
        List<FoodEntry> foodEntryList = getFoodEntriesFromDailyLog(dl);
        Double fat = 0.0;
        Double carbs = 0.0;
        Double protein = 0.0;
        Double sodium = 0.0;
        for (FoodEntry fe : foodEntryList) {
            fat += fe.getFood().getFat();
            carbs += fe.getFood().getCarbs();
            protein += fe.getFood().getProtein();
            sodium += fe.getFood().getSodium();
        }
        String output = fat.toString() + "/" + carbs.toString() + "/" + protein.toString() + "/" + sodium.toString();
        return output;
    }

    public double getWeightFromDailyLog(DailyLog dl) {
        return dl.getWeight();
    }

    public double getMinutesFromDailyLog(DailyLog dl, Exercise e) {
        List<ExerciseEntry> ee = dl.getExerciseEntries();
        double totalMins = 0;
        for (ExerciseEntry i : ee) {
            if (i.getExercise().equals(e)) {
                totalMins += i.getMinutes();
            }
        }

        return totalMins;
    }

    public double calculateTotalCalsPerLog(DailyLog dl) {
        List<FoodEntry> fe = dl.getFoodEntries();
        double totalCals = 0;
        for (FoodEntry e : fe) {
            totalCals += e.getFood().getCalories() * e.getServings();
        }

        return totalCals;
    }

    public void removeExerciseEntry(Date date, String name, double minutes) {
        // FoodEntry entry = new FoodEntry(food.getName(), servings);
        List<ExerciseEntry> e = getDailyLog(date).getExerciseEntries();
        ExerciseEntry tbRemoved = null;
        for (ExerciseEntry i : e) {

            if (i.getExercise().getName().equals(name) && i.getMinutes() == minutes) {
                tbRemoved = i;
            }
        }

        if (tbRemoved != null) {
            e.remove(tbRemoved);
            // dateToLog.remove(formatter.format(date));
        }
    }

    public DailyLog addDailyLog(Date date) {
        DailyLog log = new DailyLog(date);
        try {
            String strDate = formatter.format(date);

            for (String i : dateToLog.keySet()) {
                if (i.equals(strDate)) {
                    return dateToLog.get(i);
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return log;
    }

    public void addDailyLogEntry(DailyLog entry) {
        try {
            // if (dateToLog.containsKey(formatter.format(entry.getDate()))) {
            // // do nothing probably
            // } else {
            // dateToLog.put(formatter.format(entry.getDate()), entry);
            // }
            if (!dateToLog.containsKey(formatter.format(entry.getDate()))) {
                dateToLog.put(formatter.format(entry.getDate()), entry);

            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public double getDailyCalories(Date date) {
        if (dateToLog.containsKey(formatter.format(date))) {
            return dateToLog.get(formatter.format(date)).getCaloriesLimit();
        }
        return -1;
    }

    public double getDailyWeight(Date date) {
        if (dateToLog.containsKey(formatter.format(date))) {
            return dateToLog.get(formatter.format(date)).getWeight();
        }
        return -1;
    }

    // ask why there is a Double paramter during next meeting
    public DailyLog createDailyLog(Date date) {
        DailyLog log = getDailyLog(date);
        return log;
    }

    public DailyLog createDailyLog(Date date, Double calorieLimit) {
        DailyLog log = getDailyLog(date);
        log.setCaloriesLimit(calorieLimit);
        return log;
    }

    public void addFoodEntry(Date date, Food food, Double servings) {

        FoodEntry entry = new FoodEntry(food, servings);
        getDailyLog(date).addFoodEntry(entry);
    }

    public void addFoodEntry(Date date, String food, Double servings) {
        List<BasicFood> bfList = foodController.getBasicFoodList();
        List<Recipe> rList = foodController.getRecipeList();
        Food foodObj = null;
        for (BasicFood b : bfList) {
            if (b.getName().equals(food)) {
                foodObj = b;
            }
        }

        for (Recipe r : rList) {
            if (r.getName().equals(food)) {
                foodObj = r;
            }
        }

        if (foodObj == null) {
            return;
        }

        FoodEntry entry = new FoodEntry(foodObj, servings);
        getDailyLog(date).addFoodEntry(entry);
    }

    // creates something, tries to remove it b/c thing it creates isn't part of
    // DailyLog
    // needs to be worked on
    public void removeFoodEntry(Date date, Food food, Double servings) {
        // FoodEntry entry = new FoodEntry(food.getName(), servings);
        List<FoodEntry> e = getDailyLog(date).getFoodEntries();
        FoodEntry tbRemoved = null;
        for (FoodEntry i : e) {

            if (i.getFood().getName().equals(food.getName()) && i.getServings() == servings) {
                tbRemoved = i;
            }
        }

        if (tbRemoved != null) {
            e.remove(tbRemoved);
            // dateToLog.remove(formatter.format(date));
        }
    }

    public void removeFoodEntry(Date date, String food, Double servings) {
        // FoodEntry entry = new FoodEntry(food.getName(), servings);
        List<FoodEntry> e = getDailyLog(date).getFoodEntries();
        FoodEntry tbRemoved = null;
        for (FoodEntry i : e) {

            if (i.getFood().getName().equals(food) && i.getServings() == servings) {
                tbRemoved = i;
            }
        }

        if (tbRemoved != null) {
            e.remove(tbRemoved);
            // dateToLog.remove(formatter.format(date));

        }

    }

    public void updateCalorieLimit(Date date, Double calories) {
        getDailyLog(date).setCaloriesLimit(calories);

        for (HashMap.Entry<String, DailyLog> i : dateToLog.entrySet()) {
            if (i.getKey().equals(formatter.format(date))) {
                i.getValue().setCaloriesLimit(calories);
                System.out.println(i.getValue().getCaloriesLimit());
            }
        }
    }

    public void updateWeight(Date date, Double weight) {
        getDailyLog(date).setWeight(weight);

        for (HashMap.Entry<String, DailyLog> i : dateToLog.entrySet()) {
            if (i.getKey().equals(formatter.format(date))) {
                i.getValue().setWeight(weight);
            }
        }
    }

    // public DailyLog getDailyLog(Date date) {
    // DailyLog log = model.getDailyLog(date);
    // return log;
    // }
    public DailyLog getDailyLog(Date date) {
        String strDate = formatter.format(date);
        if (dateToLog.containsKey(strDate)) {
            // Not reading correctly
            return dateToLog.get(formatter.format(date));
        } else {
            DailyLog log = new DailyLog();
            dateToLog.put(formatter.format(date), log);
            // add to hashmap with new date
            return log;
        }
    }

    // public void getTotalCaloriesConsumed(Date date) {
    // getDailyCalories(date);
    // }

    public void getTotalWeight(Date date) {

        getDailyWeight(date);
    }

}