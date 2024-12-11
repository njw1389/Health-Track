package App.model;

import java.util.Date;
import java.util.List;

import javax.print.DocFlavor;

import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class DailyLog {

    Date date;
    double weight;
    double calorieLimit;
    List<FoodEntry> foodEntries;
    List<ExerciseEntry> exerciseEntries;

    // FoodEntry entry;

    // private String log = ".\\App\\src\\main\\resources\\log.csv";

    public DailyLog(Date date, double weight, double calorieLimit, List<FoodEntry> foodEntries,
            List<ExerciseEntry> entries) {
        this.date = date;
        this.weight = weight;
        this.calorieLimit = calorieLimit;
        this.foodEntries = foodEntries;

        this.exerciseEntries = entries;
        // entry = new FoodEntry();
    }

    public DailyLog() {
        this.date = null;
        this.foodEntries = new ArrayList<FoodEntry>();
        this.weight = 150;
        this.calorieLimit = 2000;
        this.exerciseEntries = new ArrayList<>();
        // entry = new FoodEntry();
    }

    public DailyLog(Date date) {
        this.date = date;
        this.foodEntries = new ArrayList<>();
        this.weight = 150;
        this.calorieLimit = 2000;
        this.exerciseEntries = new ArrayList<>();
    }

    public List<ExerciseEntry> getExerciseEntries() {
        return exerciseEntries;
    }

    public void setExerciseEntry(List<ExerciseEntry> newEntry) {
        exerciseEntries = newEntry;
    }

    public void addExerciseEntry(ExerciseEntry e) {
        exerciseEntries.add(e);
    }

    public void removeExerciseEntry(ExerciseEntry tbRemoved) {
        exerciseEntries.remove(tbRemoved);
    }

    public List<FoodEntry> getFoodEntries() {
        return foodEntries;
    }

    // public double getServings(){
    // return entry.getServings();
    // }

    // public String getFoodEntry(){
    // return entry.getFood();
    // }
    @Override
    public String toString() {
        String dl = "";

        dl += "\nWeight: " + weight;
        dl += "\nCalorie Limit: " + calorieLimit;
        dl += "\nFood Entries: ";
        for (int i = 0; i < foodEntries.size(); i++) {
            dl += "\t" + foodEntries.get(i).toString();
        }

        dl += "\nExercise Entries: ";
        for (int i = 0; i < exerciseEntries.size(); i++) {
            dl += "\t" + exerciseEntries.get(i).toExerciseEntryString(weight);
        }

        return dl;
    }

    public void setFoodEntry(List<FoodEntry> foodEntries1) {
        this.foodEntries = foodEntries1;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date1) {
        this.date = date1;
    }

    public void addFoodEntry(FoodEntry entry) {
        foodEntries.add(entry);
        // entry.addToCSV(, 0, 0);

    }

    public void removeFoodEntry(FoodEntry entry) {
        foodEntries.remove(entry);

    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCaloriesLimit() {
        return calorieLimit;
    }

    public void setCaloriesLimit(double calorieLimit) {
        this.calorieLimit = calorieLimit;
    }

    public static void main(String[] args) {

    }
}