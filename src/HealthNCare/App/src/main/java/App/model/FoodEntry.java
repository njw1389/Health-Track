package App.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FoodEntry {

    Food food;
    double servings;

    public FoodEntry(Food food, Double servings) {
        this.food = food;
        this.servings = servings;
    }

    public FoodEntry() {
        this.food = null;
        this.servings = 0.0;
    }

    public double getServings() {
        return servings;
    }

    public Food getFood() {
        return food;
    }

    @Override
    public String toString() {
        String fe = "";

        fe += "\n\n-->Food: " + food.getName() + "";
        fe += "\n\t----> Calories: " + food.getCalories();
        fe += "\n\t----> Servings: " + servings;

        return fe;
    }

    public void addToCSV(int year, int month, int day) {
        try {
            FileWriter fw = new FileWriter("./App/src/main/resources/log.csv", true);
            // path was not working, used relative path from VSCode
            BufferedWriter writer = new BufferedWriter(fw);

            writer.newLine();
            writer.write(String.valueOf(year));
            writer.write(",");
            writer.write(String.valueOf(month));
            writer.write(",");
            writer.write(String.valueOf(day));
            writer.write(",");
            writer.write("f");
            writer.write(food.getName());
            writer.write(String.valueOf(servings));
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}