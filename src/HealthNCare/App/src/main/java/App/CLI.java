package App;

import java.util.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import App.controller.ExerciseController;
import App.controller.FoodController;
import App.controller.LogController;
import App.model.BasicFood;
import App.model.Exercise;
import App.model.Food;
import App.view.UserInterface;
import java.text.SimpleDateFormat;

public class CLI {
    private static Scanner scanner = new Scanner(System.in);
    private static Date date;
    private static String success = "\u001B[32m" + "SUCCESS" + "\u001B[37m";
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");


    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void displayMenu() {
        System.out.println("Main Menu");
        System.out.println("\t1. Add Basic Food");
        System.out.println("\t2. Add Recipe");
        System.out.println("\t3. Change Calorie Limit");
        System.out.println("\t4. Change Weight Limit");
        System.out.println("\t5. View Daily Log (by date)");
        System.out.println("\t6. Add food to Daily Log");
        System.out.println("\t7. Delete food from Daily Log");
        System.out.println("\t8. Show List of Exercises");
        System.out.println("\t9. Add Exercise to Daily Log");
        System.out.println("\t10. Delete Exercise to Daily Log");
        System.out.println("\t11. Save Log Configuration");
        System.out.println("\t12. Exit");
    }

    public static void addBasicFood(FoodController foodController) {
        // Ask for various food info
        System.out.println("Enter Food Name: ");
        String name = scanner.nextLine();

        System.out.println("Enter Calories: ");
        double calories = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Fat: ");
        double fat = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Carbs: ");
        double carbs = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Protein: ");
        double protein = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Sodium: ");
        double sodium = Integer.parseInt(scanner.nextLine());

        foodController.addBasicFood(name, calories, fat, carbs, protein, sodium);

        System.out.print("Adding basic food");
        simulateDelay();
        System.out.println(success);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void addRecipe(FoodController foodController) {
        // Ask for various food info
        System.out.print("Enter Recipe Name: ");
        String recipeName = scanner.nextLine();

        List<Food> ingredients = new LinkedList<>();

        while (true) {
            System.out.print("Enter Food Ingredient Name: ");
            String foodName = scanner.nextLine();

            System.out.print("Enter Food Calories: ");
            double calories = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter Food Fat: ");
            double fat = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter Food Carbs: ");
            double carbs = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter Food Protein: ");
            double protein = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter Food Sodium: ");
            double sodium = Double.parseDouble(scanner.nextLine());

            Food food = new BasicFood(foodName, calories, fat, carbs, protein, sodium);

            System.out.print("Add another ingredient? (Y or N): ");
            if (scanner.nextLine().toLowerCase().equals("n")) {
                ingredients.add(food);
                break;
            } else {
                ingredients.add(food);
            }
        }

        System.out.print("Enter Recipe Servings Amount: ");
        double servings = Double.parseDouble(scanner.nextLine());

        // Create a Map instance for servings
        Map<Food, Double> servingsMap = new HashMap<>();
        for (Food ingredient : ingredients) {
            servingsMap.put(ingredient, servings);
        }

        // Make the method call with the corrected servings parameter
        foodController.addRecipe(recipeName, null);

        System.out.print("Adding Recipe ");
        simulateDelay();
        System.out.println(success);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void changeLimits(LogController logController, int limitType) {

        if (limitType == 1) {
            System.out.print("Enter Calorie Limit: ");
            Double calorieLimit = Double.parseDouble(scanner.nextLine());
            
           try{ 
            Date date = formatter.parse(scanner.nextLine());
            logController.updateCalorieLimit(date, calorieLimit);
            
            System.out.print("Changing Calorie Limit");
            }
            catch(Exception e){
                System.out.println("Error: " + e);
            }

        } else if (limitType == 2) {
            System.out.print("Enter Weight Limit: ");
            double weightLimit = Double.parseDouble(scanner.nextLine());
            try{ 
                Date date = formatter.parse(scanner.nextLine());
                logController.updateWeight(date, weightLimit);
                
                System.out.print("Changing Calorie Limit");
            }
            catch(Exception e){
                System.out.println("Error: " + e);
            }
            System.out.print("Changing Weight Limit");
        }

        // Simulate delay
        simulateDelay();
    }

    public static void viewDailyLog(LogController logController) {
        System.out.print("Enter date for log that you would like to see (YYYY-MM-DD): ");
        
        String strDate = scanner.nextLine();
        
        try {
            
            Date date = formatter.parse(strDate);
            System.out.print("Fetching Daily Log");
            simulateDelay();

            // Outputs Daily (practical code does not exist in Controller currently)
            if(logController.getDailyLog(date) == null){
                System.out.println("Null");
            }
            else{
                System.out.println(logController.getDailyLog(date).toString());
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.print("Returning to Main Menu");
        simulateDelay();
    }

    public static void addFoodToLog(LogController logController) {

        // Scanner addFoodToLogScanner = new Scanner(System.in);

        System.out.print("Enter the date you would like to use (or): ");
        String strDate = scanner.nextLine();
        // scanner.next();
        System.out.print("Enter the food you would like to log for the date: ");
        String food = scanner.nextLine();
        // scanner.next();
        System.out.print("Enter the number of servings you had of that food in that date: ");
        Double servings =scanner.nextDouble();
        scanner.nextLine();
        try{
            Date date = formatter.parse(strDate);
            logController.addFoodEntry(date, food, servings);
        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
        // logController.addFoodEntry(date, null, null);
        // addFoodToLogScanner.close();

        // scanner.close();
        // Simulate delay
        System.out.println(success);
        simulateDelay();
    }

    public static void removeFoodToLog(LogController logController) {
        System.out.println("Enter the date of the log that you would like to remove: ");
        String strDate = scanner.nextLine();
        // Date date = Date.valueOf(strDate);
        System.out.println("Enter the name of the food you would like to remove: ");
        String food = scanner.nextLine();

        System.out.println("Enter the number of servings you've had for the food: ");
        Double servings = scanner.nextDouble();

        scanner.nextLine();
        try{
            Date date = formatter.parse(strDate);
            logController.removeFoodEntry(date, food, servings);

        }
        catch(Exception e){
            System.out.println("Error: " + e);
        }
        // logController.removeFoodEntry(date, null, null);

        System.out.print("Removing food to log");
        // Simulate delay
        simulateDelay();
    }

    public static void showExercises(ExerciseController exerciseController) {

        System.out.print("Fetching Exercises");
        simulateDelay();

        List<Exercise> exerciseList = exerciseController.getExerciseList();

        System.out.println("Exercises: \n");
        for (int i=0; i<exerciseList.size(); i++) {
            System.out.println("\tExercise Name: " + exerciseList.get(i).getName());
            System.out.println("\tExercise Calories Burned: " + exerciseList.get(i).getCalories() + "calories\n");

        }

        System.out.print("Returning to Main Menu");
        // Simulate delay
        simulateDelay();
    }

    public static void addExercise(ExerciseController exerciseController) {

        System.out.print("Enter Exercise Name: ");
        String excerciseName = scanner.nextLine();

        System.out.print("Enter Exercise Calories Burned: ");
        Double calories = Double.parseDouble(scanner.nextLine());

        exerciseController.addExercise(excerciseName, calories);
        System.out.print("Adding exercise to log");
        // Simulate delay
        simulateDelay();
    }

    public static void removeExercise(ExerciseController exerciseController) {

        System.out.print("Enter Exercise Name: ");
        String excerciseName = scanner.nextLine();

        exerciseController.removeExercise(excerciseName);

        // The user shall be able to unambiguously specify which exercise to delete even if several entries have the same exercise name.
        System.out.print("Removing exercise to log");
        // Simulate delay
        simulateDelay();
    }

    // Method to simulate delay with visual indication
    public static void simulateDelay() {
        try {
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(1000); // Sleep for 1 second
            }
            System.out.println("\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void saveLog(LogController logController) {
        System.out.print("Saving Log");
        logController.updateDB();
        simulateDelay();
    }

    public static void main(String[] args) {
        // Initialize MVC components
        // Log model = new Log();
        UserInterface view = new UserInterface();
        FoodController foodController = new FoodController(view);
        ExerciseController exerciseController = new ExerciseController(view);
        LogController logController = new LogController(view, foodController, exerciseController);

        clearScreen();

        // Load Food and Log Data

        // Selects date for the log
        Date currDate = new Date(System.currentTimeMillis());
        System.out.print("Please enter a date or enter for current: ");
        String newDate = scanner.nextLine();
        if (newDate != "") {
            date = currDate;
        }

        // If Date does not exist in log
        // Make new log () --> logController.createDailyLog

        System.out.print("Adding date");
        simulateDelay();

        // Main loop for CLI
        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                addBasicFood(foodController);
            } else if (choice == 2) {
                addRecipe(foodController);
            } else if (choice == 3) {
                changeLimits(logController, 1);
            } else if (choice == 4) {
                changeLimits(logController, 2);
            } else if (choice == 5) {
                // View Daily Log
                viewDailyLog(logController);
            } else if (choice == 6) {
                // Add food to Daily Log
                addFoodToLog(logController);
            } else if (choice == 7) {
                // Delete Food From Daily Log
                removeFoodToLog(logController);
            } else if (choice == 8) {
                // Show List of Exercises
                showExercises(exerciseController);
            } else if (choice == 9) {
                // Add Exercise and # of minutes
                addExercise(exerciseController);
            } else if (choice == 10) {
                // Delete Exercise 
                removeExercise(exerciseController);
            } else if (choice == 11) {
                saveLog(logController);
            } else if (choice == 12) {
                break;
            } else {
                System.out.println("Invalid choice! Please try again.");
            }
        }
        // Close scanner
        scanner.close();
    }
}