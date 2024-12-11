package App.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;

public class Recipe implements Food {

    String name;
    List<Food> foods;
    Map<Food, Double> servings;
    static final String FILE = "src/main/resources/foods.csv";

    public Recipe(String name, List<Food> foods, Map<Food, Double> servings) {
        this.name = name;
        this.foods = foods;
        this.servings = servings;
    }

    public void addIngredident(Food food, double servings) {
        this.foods.add(food);
        this.servings.put(food, servings);
    }

    public void removeIngredident(Food food) {
        this.foods.remove(food);
        this.servings.remove(food);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCalories() {
        double totalCalories = 0.0;

        for (int i = 0; i < foods.size(); i++) {
            totalCalories += foods.get(i).getCalories() * servings.get(foods.get(i));
        }

        return totalCalories;
    }

    @Override
    public double getFat() {
        double totalFat = 0.0;

        for (int i = 0; i < foods.size(); i++) {
            totalFat += foods.get(i).getFat() * servings.get(foods.get(i));
        }

        return totalFat;
    }

    @Override
    public double getCarbs() {
        double totalCarbs = 0.0;

        for (int i = 0; i < foods.size(); i++) {
            totalCarbs += foods.get(i).getCarbs() * servings.get(foods.get(i));
        }

        return totalCarbs;
    }

    @Override
    public double getProtein() {
        double totalProtein = 0.0;

        for (int i = 0; i < foods.size(); i++) {
            totalProtein += foods.get(i).getProtein() * servings.get(foods.get(i));
        }

        return totalProtein;
    }

    @Override
    public double getSodium() {
        double totalSodium = 0.0;

        for (int i = 0; i < foods.size(); i++) {
            totalSodium += foods.get(i).getSodium() * servings.get(foods.get(i));
        }

        return totalSodium;
    }

    public List<Recipe> readCSV() {
        List<Recipe> recipeList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE);
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> allData = csvReader.readAll(); // read csv, store each line list of strings
            for (int i = 0; i < allData.size(); i++) {
                String[] row = allData.get(i); // get i row of data
                String foodType = row[0]; // row[0] = r or b
                String recipeName = row[1];
                if (foodType.equals("r")) {
                    Recipe recipe = new Recipe(recipeName, new ArrayList<>(), new HashMap<>());
                    for (int p = 2; p < row.length; p += 2) {
                        String foodIngredientName = row[p]; // ingredient in recipe
                        if (!foodIngredientName.isEmpty()) {
                            Double foodServings = Double.parseDouble(row[p + 1]);
                            // System.out.println("(In Recipe) Ingredient Food Name: " +
                            // foodIngredientName);
                            // System.out.println("(In Recipe) Ingredient Servings: " + foodServings);
                            for (int j = 0; j < allData.size(); j++) { // if recipe, loop through .csv again
                                String[] searchRow = allData.get(j); // get j row
                                String foodDelimiter = searchRow[0]; // r or b
                                String searchFood = searchRow[1]; // check name of food

                                if (foodIngredientName.equalsIgnoreCase(searchFood)) {// ingredient matches with food
                                    // System.out.println("(Searching) Found Food: " + searchFood); // add to recipe
                                    // object
                                    // System.out.println("Recipe Name: " + recipeName);
                                    if (foodDelimiter.equals("r")) {
                                        // create another function that passes in all data

                                        recipe = parseRecipe(allData, foodIngredientName, recipe);

                                    } else if (foodDelimiter.equals("b")) {
                                        // System.out.println("Its a basic food! ---> " + searchFood);
                                        Double calories = Double.parseDouble(searchRow[2]);
                                        Double fat = Double.parseDouble(searchRow[3]);
                                        Double carbs = Double.parseDouble(searchRow[4]);
                                        Double protein = Double.parseDouble(searchRow[5]);
                                        Double sodium = Double.parseDouble(searchRow[6]);

                                        BasicFood bf = new BasicFood(searchFood, calories, fat, carbs, protein, sodium);
                                        recipe.addIngredident(bf, foodServings);
                                    }
                                    // System.out.println("(Searching) Found Food Calories: " + searchRow[2]);
                                }
                            }
                            // recipeList.add(bf);
                        }
                    }
                    // System.out.println("FINAL RECIPE OBJECT NAME: " + recipe.getName());
                    // System.out.println("FINAL RECIPE OBJECT CALORIES: " + recipe.getCalories());
                    recipeList.add(recipe);
                }

            }
            csvReader.close();
        } catch (Exception e) {
            System.out.println("!!! ERROR !!! ---> " + e);
        }
        return recipeList;
    }

    public Recipe parseRecipe(List<String[]> allData, String ingredient, Recipe recipe) {
        for (int i = 0; i < allData.size(); i++) { // take csv and loop through each line
            String[] row = allData.get(i); // put each line in a row which is a string list
            String matchFood = row[1];
            // System.out.println(row[0] + " " + row[1] + " " + row[2]);
            if (matchFood.equalsIgnoreCase(ingredient)) { // match food name with passed in ingredient
                                                          // (should be recipe)
                for (int j = 2; j < row.length; j += 2) {
                    String ingredientInRecipe = row[j];
                    if (!ingredientInRecipe.isEmpty()) {
                        Double foodServings = Double.parseDouble(row[j + 1]);
                        for (int k = 0; k < allData.size(); k++) {
                            String[] searchRow = allData.get(k);
                            if (searchRow[1].equalsIgnoreCase(ingredientInRecipe)) {
                                if (searchRow[0].equals("b")) {
                                    // System.out.println("\n ONE PASS");
                                    // System.out.println("Food Name: " + searchRow[1] + " <<< Calories: " +
                                    // searchRow[2]);
                                    BasicFood bf = new BasicFood(searchRow[1], Double.parseDouble(searchRow[2]),
                                            Double.parseDouble(searchRow[3]), Double.parseDouble(searchRow[4]),
                                            Double.parseDouble(searchRow[5]), Double.parseDouble(searchRow[6]));
                                    recipe.addIngredident(bf, foodServings);
                                } else if (searchRow[0].equals("r")) {
                                    parseRecipe(allData, ingredientInRecipe, recipe);
                                }
                            }
                        }
                    }
                }
            }
        }
        return recipe;
    }

    // public static void main(String[] args) {
    // Recipe r = new Recipe(null, null, null);
    // r.readCSV();
    // }
}

// System.out.println("Its a recipe! ---> " + searchFood);
// String nestedRecipeName = searchFood;
// for (int k = 2; k < row.length; k += 2) {
// String nestedIngredientName = row[k]; // ingredient in recipe
// if (!nestedIngredientName.isEmpty()) {
// Double nestedFoodServings = Double.parseDouble(row[k + 1]);
// for (int g = 0; g < allData.size(); g++) {
// String[] nestedSearchRow = allData.get(g);
// // String nestedFoodDelimiter = nestedSearchRow[0];
// String nestedSearchFood = nestedSearchRow[1];
// if (nestedIngredientName.equalsIgnoreCase(nestedSearchFood)) {
// System.out.println("\nA MATCH !!!: ");
// System.out.println("Name of Food: " + nestedSearchRow[1]);
// System.out.println("Calories of Food: " + nestedSearchRow[2]);
// System.out.println("yada of Food: " + nestedSearchRow[3]);
// System.out
// .println("Yuda of Food: " + nestedSearchRow[4] + "\n");
// }
// }
// }
// //eventually one recipe object will be put in the list
// }