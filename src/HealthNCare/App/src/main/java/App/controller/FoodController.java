package App.controller;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import App.model.BasicFood;
import App.model.Food;
import App.model.Recipe;
// import App.view.TestFood;
import App.view.UserInterface;

public class FoodController {
    BasicFood basicFood = new BasicFood(null, 0.0, 0.0, 0.0, 0.0, 0.0);
    Recipe recipe = new Recipe(null, new ArrayList<>(), new HashMap<>());

    List<BasicFood> bfList = new ArrayList<>();
    List<Recipe> recipeList = new ArrayList<>();

    UserInterface view;
    static final String FILE = "src/main/resources/foods.csv";

    public FoodController(UserInterface view) {
        this.view = view;
    }

    public void init() {
        bfList = basicFood.readCSV();
        recipeList = recipe.readCSV();
    }

    public int addBasicFood(String name, Double calories, Double fat, Double carbs, Double protein, Double sodium) {
        // Check first if inputted name is duplicate in CSV
        List<BasicFood> bfList = basicFood.readCSV();
        boolean duplicate = false;
        for (BasicFood bf : bfList) {
            if (name.equals(bf.getName())) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            // take data and write to csv
            try {
                FileWriter fileWriter = new FileWriter(FILE, true);
                CSVWriter csvWriter = new CSVWriter(fileWriter,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
                String[] data = { "b", name, calories.toString(), fat.toString(),
                        carbs.toString(), protein.toString(),
                        sodium.toString() };
                csvWriter.writeNext(data);
                csvWriter.close();
                return 0; // return 0 to indicate program adding success
            } catch (Exception e) {
                System.out.println("!!! ERROR addBasicFood !!! ---> " + e);
                return 1;
            }
        } else {
            System.out.println("!!! ERROR !!! ---> Duplicate Detected!");
            return 1; // return 1 to indicate failure
        }

        // notify model to update itself
    }

    public int addRecipe(String name, String ingredients) {
        // Check first if inputted name is duplicate in CSV
        List<Recipe> recipeList = recipe.readCSV();
        boolean duplicate = false;
        for (Recipe recipe : recipeList) {
            if (name.equals(recipe.getName())) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            // take data and write to csv
            try {
                FileWriter fileWriter = new FileWriter(FILE, true);
                CSVWriter csvWriter = new CSVWriter(fileWriter,
                        '\t', // customer separator to account for ""
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
                String[] data = { "r," + name + "," + ingredients };
                csvWriter.writeNext(data);
                csvWriter.close();
                return 0;
            } catch (Exception e) {
                System.out.println("!!! ERROR addRecipe !!! ---> " + e);
                return 1;
            }
        } else {
            System.out.println("!!! ERROR !!! ---> Duplicate Detected!");
            return 1; // return 1 to indicate failure
        }

        // notify model to update itself
    }

    public int removeFood(String removeName) {
        // take data and delete from csv
        try {
            FileReader fileReader = new FileReader(FILE);
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> allData = csvReader.readAll();
            for (int i = 0; i < allData.size(); i++) {
                String[] row = allData.get(i);
                String foodName = row[1]; // row[1] will always be name of food
                if (removeName.equals(foodName)) {
                    allData.remove(i);
                }
            }

            FileWriter fileWriter = new FileWriter(FILE);
            CSVWriter csvWriter = new CSVWriter(fileWriter,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            csvWriter.writeAll(allData);

            csvWriter.close();
            csvReader.close();
            return 0; /// indication of success
        } catch (Exception e) {
            System.out.println("!!! ERROR removeFood !!! ---> " + e);
            return 1; // indication of failure
        }
        // notify model to update itself
    }

    // public double getFoodCalories(){

    // }

    public List<BasicFood> getBasicFoodList() {
        List<BasicFood> basicFoodList = basicFood.readCSV();

        return basicFoodList;

        // for (BasicFood listFood : basicFoodList) {
        // // Example of what update list would look like
        // // view.updateBasicFoodList(listFood.getName(), listFood.getCalories(),
        // // listFood.getFat(), listFood.getCarbs(),
        // // listFood.getProtein(), listFood.getSodium());
        // }
    }


    public List<Recipe> getRecipeList() {
        List<Recipe> recipeList = recipe.readCSV();

        return recipeList;
        // for (Recipe listFood : recipeList) {
        // // Do view methods here
        // // System.out.println("Recipe Name: " + listFood.getName());
        // // System.out.println("Recipe Calories: " + listFood.getCalories());
        // }
    }
}

// view.updateBasicFoodList(listFood.getName(), listFood.getCalories(),
// listFood.getFat(), listFood.getCarbs(),
// listFood.getProtein(), listFood.getSodium());