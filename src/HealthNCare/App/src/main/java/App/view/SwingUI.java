package App.view;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;

import App.controller.ExerciseController;
import App.controller.FoodController;
import App.controller.LogController;
import App.model.BasicFood;
import App.model.Exercise;
import App.model.ExerciseEntry;
import App.model.FoodEntry;
import App.model.Recipe;
import App.model.DailyLog;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

public class SwingUI {
    private FoodController foodController;
    private LogController logController;
    private ExerciseController exerciseController;

    private JFrame frame;
    private JTabbedPane tabbedPane;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/m/dd");

    public SwingUI(FoodController foodController, LogController logController, ExerciseController exerciseController) {
        foodController.init();
        exerciseController.init();

        this.foodController = foodController;
        this.logController = logController;
        this.exerciseController = exerciseController;

        frame = new JFrame("HealthNCare");

        // Add window listener
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "Would You Like to Save Your Daily Log Before Exiting?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Call the method to save the daily log
                    logController.updateDB();
                }
            }
        });

        tabbedPane = new JTabbedPane();

        // Create tabs for different categories
        JPanel foodPanel = createFoodPanel();
        JPanel exercisePanel = createExercisePanel();
        JPanel logPanel = createLogPanel();
        JPanel viewPanel = createViewPanel();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Food Actions", foodPanel);
        tabbedPane.addTab("Exercise Actions", exercisePanel);
        tabbedPane.addTab("Log Actions", logPanel);
        tabbedPane.addTab("View Actions", viewPanel);

        // Set preferred size for tabbedPane
        tabbedPane.setPreferredSize(new Dimension(500, 300));

        // Add tabbed pane to the frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    private JPanel createFoodPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        JButton addBasicFoodButton = new JButton("Add Basic Food");
        JButton addRecipeButton = new JButton("Add Recipe");
        JButton removeFoodButton = new JButton("Remove Food");
        panel.add(addBasicFoodButton);
        panel.add(addRecipeButton);
        panel.add(removeFoodButton);

        // Add action listeners to buttons
        addBasicFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a JPanel to hold the input fields
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                // Create the input fields and labels
                JTextField nameField = new JTextField();
                JTextField caloriesField = new JTextField();
                JTextField fatField = new JTextField();
                JTextField carbsField = new JTextField();
                JTextField proteinField = new JTextField();
                JTextField sodiumField = new JTextField();

                JLabel nameLabel = new JLabel("Food Name:");
                JLabel caloriesLabel = new JLabel("Calories:");
                JLabel fatLabel = new JLabel("Fat:");
                JLabel carbsLabel = new JLabel("Carbs:");
                JLabel proteinLabel = new JLabel("Protein:");
                JLabel sodiumLabel = new JLabel("Sodium:");

                // Add the labels and fields to the input panel
                inputPanel.add(nameLabel);
                inputPanel.add(nameField);
                inputPanel.add(caloriesLabel);
                inputPanel.add(caloriesField);
                inputPanel.add(fatLabel);
                inputPanel.add(fatField);
                inputPanel.add(carbsLabel);
                inputPanel.add(carbsField);
                inputPanel.add(proteinLabel);
                inputPanel.add(proteinField);
                inputPanel.add(sodiumLabel);
                inputPanel.add(sodiumField);

                try {
                    // Show the input panel in a JOptionPane
                    int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Add Basic Food",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        // Get the values from the input fields
                        String name = nameField.getText();
                        double calories = Double.parseDouble(caloriesField.getText());
                        double fat = Double.parseDouble(fatField.getText());
                        double carbs = Double.parseDouble(carbsField.getText());
                        double protein = Double.parseDouble(proteinField.getText());
                        double sodium = Double.parseDouble(sodiumField.getText());

                        // Send the string to the controller
                        int complete = foodController.addBasicFood(name, calories, fat, carbs, protein, sodium);
                        if (complete == 0) {
                            JOptionPane.showMessageDialog(frame, "Food Added", "Success!",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Error Adding", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception bfException) {
                    JOptionPane.showMessageDialog(frame, bfException, "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        addRecipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gather input from the user using JOptionPane dialogs
                String ingredients = "";
                String recipeName = JOptionPane.showInputDialog(frame, "Enter Recipe Name:");

                boolean addAnotherIngredient = true;
                while (addAnotherIngredient) {
                    String[] options = { "Add existing Basic Food", "Add existing Recipe" };
                    String selectedOption = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:", "Input",
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (selectedOption != null && selectedOption.equals(options[0])) {
                        List<BasicFood> bfList = foodController.getBasicFoodList();
                        ArrayList<String> optionsList = new ArrayList<>();
                        for (BasicFood bf : bfList) {
                            optionsList.add(bf.getName());
                        }
                        String[] bfOptions = optionsList.toArray(new String[0]);
                        String selectedBf = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:", "Input",
                                JOptionPane.QUESTION_MESSAGE, null, bfOptions, bfOptions[0]);
                        double servings = Double
                                .parseDouble(JOptionPane.showInputDialog(frame, "Enter Servings:"));
                        ingredients = ingredients + selectedBf + "," + Double.toString(servings) + ",";
                    } else if (selectedOption != null && selectedOption.equals(options[1])) {
                        List<Recipe> recipeList = foodController.getRecipeList();
                        ArrayList<String> optionsList = new ArrayList<>();
                        for (Recipe recipe : recipeList) {
                            optionsList.add(recipe.getName());
                        }
                        String[] recipeOptions = optionsList.toArray(new String[0]);
                        String selectedRecipe = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:",
                                "Input",
                                JOptionPane.QUESTION_MESSAGE, null, recipeOptions, recipeOptions[0]);
                        double servings = Double
                                .parseDouble(JOptionPane.showInputDialog(frame, "Enter Servings:"));
                        ingredients = ingredients + selectedRecipe + "," + Double.toString(servings) + ",";
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error Adding", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    System.out.println(ingredients);
                    int choice = JOptionPane.showConfirmDialog(frame, "Add another ingredient?", "Continue",
                            JOptionPane.YES_NO_OPTION);
                    if (choice != JOptionPane.YES_OPTION) {
                        addAnotherIngredient = false;
                    }
                }

                // Send the recipe name and ingredients string to the controller
                foodController.addRecipe(recipeName, ingredients);

                // Provide feedback to the user (simulated delay and success message)
                JOptionPane.showMessageDialog(frame, "Recipe added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        removeFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                List<BasicFood> bfList = foodController.getBasicFoodList();
                List<Recipe> recipeList = foodController.getRecipeList();
                ArrayList<String> optionsList = new ArrayList<>();
                for (BasicFood bf : bfList) {
                    optionsList.add(bf.getName());
                }

                for (Recipe recipe : recipeList) {
                    optionsList.add(recipe.getName());
                }
                String[] options = optionsList.toArray(new String[0]);
                String selectedFood = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:", "Input",
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                int status = foodController.removeFood(selectedFood);
                if (status == 0) {
                    JOptionPane.showMessageDialog(frame, "Food Removed", "Success!",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error Removing", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return panel;
    }

    private JPanel createExercisePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        JButton addExerciseButton = new JButton("Add Exercise");
        JButton deleteExerciseButton = new JButton("Delete Exercise");
        panel.add(addExerciseButton);
        panel.add(deleteExerciseButton);

        // Add Exercise Button Action Listener
        addExerciseButton.addActionListener(e -> {
            String exerciseName = JOptionPane.showInputDialog(frame, "Enter Exercise Name:");
            String caloriesStr = JOptionPane.showInputDialog(frame, "Enter Calories Burned per Hour:");

            if (exerciseName != null && !exerciseName.isEmpty() && caloriesStr != null && !caloriesStr.isEmpty()) {
                double calories = Double.parseDouble(caloriesStr);
                int status = exerciseController.addExercise(exerciseName, calories);
                if (status == 0) {
                    JOptionPane.showMessageDialog(frame, "Exercise Added", "Success!",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error Adding", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Remove Exercise Button Action Listener
        deleteExerciseButton.addActionListener(e -> {
            List<Exercise> exerciseList = exerciseController.getExerciseList();
            ArrayList<String> optionsList = new ArrayList<>();
            for (Exercise exercise : exerciseList) {
                optionsList.add(exercise.getName());
            }
            String[] exerciseOptions = optionsList.toArray(new String[0]);
            String selectedExercise = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:",
                    "Input",
                    JOptionPane.QUESTION_MESSAGE, null, exerciseOptions, exerciseOptions[0]);

            if (selectedExercise != null && !selectedExercise.isEmpty()) {
                int status = exerciseController.removeExercise(selectedExercise);
                if (status == 0) {
                    JOptionPane.showMessageDialog(frame, "Exercise Removed", "Success!",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error Deleting", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        JButton addFoodToLogButton = new JButton("Add Food to Daily Log");
        JButton deleteFoodFromLogButton = new JButton("Delete Food from Daily Log");
        JButton addExerciseLogButton = new JButton("Add Exercise to Daily Log");
        JButton deleteExerciseLogButton = new JButton("Delete Exercise from Daily Log");
        JButton changeCalorieLimitButton = new JButton("Change Calorie Limit of Daily Log");
        JButton changeWeightButton = new JButton("Change Weight of Daily Log");
        JButton saveLogConfigurationButton = new JButton("Save Log Configuration");
        panel.add(addFoodToLogButton);
        panel.add(deleteFoodFromLogButton);
        panel.add(addExerciseLogButton);
        panel.add(deleteExerciseLogButton);
        panel.add(changeCalorieLimitButton);
        panel.add(changeWeightButton);
        panel.add(saveLogConfigurationButton);

        // Issues reading here, no response back no update to log.csv
        addFoodToLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Prompt the user for a date input
                    Date date = null;
                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                    JLabel year = new JLabel("Year (YYYY):");
                    JLabel month = new JLabel("Month (MM):");
                    JLabel day = new JLabel("Day (DD):");

                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    inputPanel.add(year);
                    inputPanel.add(yearField);
                    inputPanel.add(month);
                    inputPanel.add(monthField);
                    inputPanel.add(day);
                    inputPanel.add(dayField);

                    try {
                        // Show the input panel in a JOptionPane
                        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Get the values from the input fields
                            String yearValue = yearField.getText();
                            String monthValue = monthField.getText();
                            String dayValue = dayField.getText();
                            String dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                            try{date = formatter.parse(dateValue);
}                           catch(Exception f){
                                date = logController.getMostRecentDate();
                            }
                        }
                        
                        List<BasicFood> bfList = foodController.getBasicFoodList();
                        List<Recipe> recipeList = foodController.getRecipeList();
                        ArrayList<String> optionsList = new ArrayList<>();
                        for (BasicFood bf : bfList) {
                            optionsList.add(bf.getName());
                        }

                        for (Recipe recipe : recipeList) {
                            optionsList.add(recipe.getName());
                        }
                        String[] options = optionsList.toArray(new String[0]);
                        String selectedFood = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:", "Input",
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        double servings = Double
                                .parseDouble(JOptionPane.showInputDialog(frame, "Enter Servings:"));
                        logController.getDailyLog(date);
                        logController.addFoodEntry(date, selectedFood, servings);

                    } catch (Exception exception) {
                        date = logController.getMostRecentDate();

                        JOptionPane.showMessageDialog(frame, exception, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    // Handle other exceptions
                    JOptionPane.showMessageDialog(frame, "An error occurred. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(frame, "Food added to log -> \"Save Log Configuration\" to save",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        deleteFoodFromLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    Date date = null;
                    try {
                        // Prompt the user for a date input

                        JPanel inputPanel = new JPanel();
                        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                        JLabel year = new JLabel("Year (YYYY):");
                        JLabel month = new JLabel("Month (MM):");
                        JLabel day = new JLabel("Day (DD):");

                        JTextField yearField = new JTextField();
                        JTextField monthField = new JTextField();
                        JTextField dayField = new JTextField();

                        inputPanel.add(year);
                        inputPanel.add(yearField);
                        inputPanel.add(month);
                        inputPanel.add(monthField);
                        inputPanel.add(day);
                        inputPanel.add(dayField);
                        try {
                            // Show the input panel in a JOptionPane
                            int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                    JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                // Get the values from the input fields
                                String yearValue = yearField.getText();
                                String monthValue = monthField.getText();
                                String dayValue = dayField.getText();
                                String dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                                try{
                                    date = formatter.parse(dateValue);
                                }
                                catch(Exception f){
                                    date = logController.getMostRecentDate();
                                }
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, ex, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ed) {
                        JOptionPane.showMessageDialog(frame, ed, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    // Retrieve the list of foods entered for the given date
                    // List<FoodEntry> foodEntries =
                    // logController.getDailyLog(date).getFoodEntries();

                    // Create a new window to display the list of foods for selection
                    JFrame foodListFrame = new JFrame("Select Food Entry to Delete for " + date);
                    JPanel panel = new JPanel(new BorderLayout());

                    // Create a JList to display the food
                    List<FoodEntry> foodEntryList = logController.getDailyLog(date).getFoodEntries();

                    // Populate the food entry list model with food entries
                    String[] array = new String[foodEntryList.size()];
                    for (int i = 0; i < foodEntryList.size(); i++) {
                        array[i] = foodEntryList.get(i).toString();
                    }
                    if (foodEntryList.size() != 0) {
                        String choice = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:", "Input",
                                JOptionPane.QUESTION_MESSAGE, null, array, array[0]);

                        int foodIndex = 0;

                        for (int i = 0; i < array.length; i++) {
                            if (array[i].equals(choice)) {
                                foodIndex = i;
                            }
                        }

                        logController.removeFoodEntry(date, foodEntryList.get(foodIndex).getFood().getName(),
                                foodEntryList.get(foodIndex).getServings());
                        JOptionPane.showMessageDialog(frame,
                                "Food removed -> \"Save Log Configuration\" to save",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "There are no food entries for the date that was input. Please input a valid date.",
                                "Alert", JOptionPane.WARNING_MESSAGE);

                    }

                    // Dispose the food list frame
                    foodListFrame.dispose();
                } catch (Exception ex) {
                    // Handle exceptions
                    JOptionPane.showMessageDialog(frame, "Error deleting food entry from log: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addExerciseLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = null;
                try {
                    // Prompt the user for a date input

                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                    JLabel year = new JLabel("Year (YYYY):");
                    JLabel month = new JLabel("Month (MM):");
                    JLabel day = new JLabel("Day (DD):");

                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    inputPanel.add(year);
                    inputPanel.add(yearField);
                    inputPanel.add(month);
                    inputPanel.add(monthField);
                    inputPanel.add(day);
                    inputPanel.add(dayField);
                    try {
                        // Show the input panel in a JOptionPane
                        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Get the values from the input fields
                            String yearValue = yearField.getText();
                            String monthValue = monthField.getText();
                            String dayValue = dayField.getText();
                            String dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                            try{
                                date = formatter.parse(dateValue);
                            }
                            catch(Exception f){
                                date = logController.getMostRecentDate();
                            }
                        } else {
                            date = new Date(System.currentTimeMillis());
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ed) {
                    JOptionPane.showMessageDialog(frame, ed, "Error", JOptionPane.ERROR_MESSAGE);
                }
                List<Exercise> exerciseList = exerciseController.getExerciseList();
                String[] array = new String[exerciseList.size()];

                // JPanel inputPanel = new JPanel();
                // inputPanel.setLayout(new GridLayout(1, 1, 10, 10));

                // JLabel minutesLbl = new JLabel("Minutes");
                // JTextField minutesField = new JTextField();

                // inputPanel.add(minutesLbl);
                // inputPanel.add(minutesField);

                for (int i = 0; i < exerciseList.size(); i++) {
                    array[i] = exerciseList.get(i).toString();
                }

                if (exerciseList.size() != 0) {
                    String selectedOption = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:", "Input",
                            JOptionPane.QUESTION_MESSAGE, null, array, array[0]);
                    int index = 0;

                    for (int j = 0; j < array.length; j++) {
                        if (array[j].equals(selectedOption)) {
                            index = j;
                        }
                    }

                    String minutesStr = JOptionPane.showInputDialog(frame,
                            "Enter length of exercise in minutes.");
                    Double minutes;

                    try {
                        minutes = Double.parseDouble(minutesStr);
                        logController.addExerciseEntry(date, exerciseList.get(index).getName(), minutes);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Invalid number of minutes.",
                                "Alert", JOptionPane.WARNING_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(frame,
                            "There are no exercises for the date that was inputted. Please input a valid date.",
                            "Alert", JOptionPane.WARNING_MESSAGE);

                }
                JOptionPane.showMessageDialog(frame, "Exercise added to log -> \"Save Log Configuration\" to save",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        deleteExerciseLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = null;
                try {
                    // Prompt the user for a date input

                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                    JLabel year = new JLabel("Year (YYYY):");
                    JLabel month = new JLabel("Month (MM):");
                    JLabel day = new JLabel("Day (DD):");

                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    inputPanel.add(year);
                    inputPanel.add(yearField);
                    inputPanel.add(month);
                    inputPanel.add(monthField);
                    inputPanel.add(day);
                    inputPanel.add(dayField);
                    try {
                        // Show the input panel in a JOptionPane
                        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Get the values from the input fields
                            String yearValue = yearField.getText();
                            String monthValue = monthField.getText();
                            String dayValue = dayField.getText();
                            String dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                            try{
                                date = formatter.parse(dateValue);
                            }
                            catch(Exception f){
                                date = logController.getMostRecentDate();
                            }
                        } else {
                            // date = new Date(System.currentTimeMillis());
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ed) {
                    JOptionPane.showMessageDialog(frame, ed, "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Retrieve the list of foods entered for the given date
                // List<FoodEntry> foodEntries =
                // logController.getDailyLog(date).getFoodEntries();

                // Create a new window to display the list of foods for selection
                JFrame exerciseListFrame = new JFrame("Select Exercise Entry to Delete for " + date);
                JPanel panel = new JPanel(new BorderLayout());

                // Create a JList to display the food
                List<ExerciseEntry> exerciseEntryList = logController.getDailyLog(date).getExerciseEntries();

                // Populate the food entry list model with food entries
                String[] array = new String[exerciseEntryList.size()];

                for (int i = 0; i < exerciseEntryList.size(); i++) {
                    array[i] = exerciseEntryList.get(i).toString();
                }
                if (exerciseEntryList.size() != 0) {
                    String choice = (String) JOptionPane.showInputDialog(frame, "Chooose an Option:", "Input",
                            JOptionPane.QUESTION_MESSAGE, null, array, array[0]);

                    int exerciseIndex = 0;

                    for (int i = 0; i < array.length; i++) {
                        if (array[i].equals(choice)) {
                            exerciseIndex = i;
                        }
                    }

                    logController.removeExerciseEntry(date,
                            exerciseEntryList.get(exerciseIndex).getExercise().getName(),
                            exerciseEntryList.get(exerciseIndex).getMinutes());
                    JOptionPane.showMessageDialog(frame,
                            "Delete exercise successful -> \"Save Log Configuration\" to save",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "There are no exericse entries for the date that was input. Please input a valid date.",
                            "Alert", JOptionPane.WARNING_MESSAGE);

                }
            }
        });

        // Add action listener to the Calorie Limit button
        changeCalorieLimitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for a date input
                Date date = null;
                try {
                    // Prompt the user for a date input

                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                    JLabel year = new JLabel("Year (YYYY):");
                    JLabel month = new JLabel("Month (MM):");
                    JLabel day = new JLabel("Day (DD):");

                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    inputPanel.add(year);
                    inputPanel.add(yearField);
                    inputPanel.add(month);
                    inputPanel.add(monthField);
                    inputPanel.add(day);
                    inputPanel.add(dayField);
                    try {
                        // Show the input panel in a JOptionPane
                        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Get the values from the input fields
                            String yearValue = yearField.getText();
                            String monthValue = monthField.getText();
                            String dayValue = dayField.getText();
                            String dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                            try{
                                date = formatter.parse(dateValue);
                            }
                            catch(Exception f){
                                date = logController.getMostRecentDate();
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ed) {
                    JOptionPane.showMessageDialog(frame, ed, "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Prompt the user for the new calorie limit
                String newCalorieLimitStr = JOptionPane.showInputDialog(frame, "Enter New Calorie Limit:");
                double newCalorieLimit = Double.parseDouble(newCalorieLimitStr);

                try {
                    logController.updateCalorieLimit(date, newCalorieLimit);
                } catch (Exception f) {
                    System.out.println("Exception: " + f);
                }
                // Call the method to update the calorie limit for the specified date
                // logController.updateCalorieLimit(date, newCalorieLimit);

                // Provide feedback to the user
                JOptionPane.showMessageDialog(frame, "Calorie limit updated -> \"Save Log Configuration\" to save",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add action listener to the Weight button
        changeWeightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for a date input
                Date date = null;
                try {
                    // Prompt the user for a date input

                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                    JLabel year = new JLabel("Year (YYYY):");
                    JLabel month = new JLabel("Month (MM):");
                    JLabel day = new JLabel("Day (DD):");

                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    inputPanel.add(year);
                    inputPanel.add(yearField);
                    inputPanel.add(month);
                    inputPanel.add(monthField);
                    inputPanel.add(day);
                    inputPanel.add(dayField);
                    try {
                        // Show the input panel in a JOptionPane
                        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Get the values from the input fields
                            String yearValue = yearField.getText();
                            String monthValue = monthField.getText();
                            String dayValue = dayField.getText();
                            String dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                            try{
                                date = formatter.parse(dateValue);
                            }
                            catch(Exception f){
                                date = logController.getMostRecentDate();
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ed) {
                    JOptionPane.showMessageDialog(frame, ed, "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Prompt the user for the new weight limit
                String newWeightStr = JOptionPane.showInputDialog(frame, "Enter New Weight:");
                double newWeight = Double.parseDouble(newWeightStr);
                try {
                    logController.updateWeight(date, newWeight);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex);
                }

                // Call the method to update the weight for the specified date
                // logController.updateWeightLimit(date, newWeight);

                // Provide feedback to the user
                JOptionPane.showMessageDialog(frame, "Weight Added -> \"Save Log Configuration\" to save", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        saveLogConfigurationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method to update the database/log configuration
                logController.updateDB();

                // Provide feedback to the user
                JOptionPane.showMessageDialog(frame, "Log configuration saved successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return panel;
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        JButton viewDailyLogButton = new JButton("View Daily Log (by date)");
        JButton viewDailyLogChartButton = new JButton("View Daily Log Chart");

        panel.add(viewDailyLogButton);
        panel.add(viewDailyLogChartButton);

        viewDailyLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user for a date input
                DailyLog dl = null;
                try {
                    // Prompt the user for a date input
                    Date date = null;
                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                    JLabel year = new JLabel("Year (YYYY):");
                    JLabel month = new JLabel("Month (MM):");
                    JLabel day = new JLabel("Day (DD):");

                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    inputPanel.add(year);
                    inputPanel.add(yearField);
                    inputPanel.add(month);
                    inputPanel.add(monthField);
                    inputPanel.add(day);
                    inputPanel.add(dayField);
                    try {
                        // Show the input panel in a JOptionPane
                        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Get the values from the input fields
                            String yearValue = yearField.getText();
                            String monthValue = monthField.getText();
                            String dayValue = dayField.getText();
                            String dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                            try{
                                date = formatter.parse(dateValue);
                            }
                            catch(Exception f){
                                date = logController.getMostRecentDate();
                            }
                            dl = logController.getDailyLog(date);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    // Handle other exceptions
                    JOptionPane.showMessageDialog(frame, "An error occurred. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                Double totalCals = logController.calculateTotalCalsPerLog(dl);
                String totalCalString = "\n\nTotal Calories Consumed: " + totalCals.toString();

                Double totalCalBurned = logController.calculateCaloriesBurned(dl);
                String totalCalBurnedString = String.format("\n\nTotal Calories Burned: %.2f", totalCalBurned);

                Double netCalories = totalCals - totalCalBurned;
                String netCaloriesString = String.format("\n\nNet Calories: %.2f", netCalories);

                if (netCalories > dl.getCaloriesLimit()) {
                    netCaloriesString += " \n---> OVER LIMIT! <---";
                }

                String nutritionFromDailyLog = logController.getDailyLogChartInfo(dl);
                String[] nutrition = nutritionFromDailyLog.split("/");
                Double fat = Double.parseDouble(nutrition[0]);
                Double carbs = Double.parseDouble(nutrition[1]);
                Double protein = Double.parseDouble(nutrition[2]);
                Double sodium = Double.parseDouble(nutrition[3]);
                Double totalGrams = fat + carbs + protein;

                Double fatPercent = (fat / totalGrams) * 100;
                Double carbsPercent = (carbs / totalGrams) * 100;
                Double proteinPercent = (protein / totalGrams) * 100;

                String nutritionBreakdown = String.format(
                        "\n\nNutritional Breakdown:\n----> Fat Percentage: %.2f%%\n----> Carbs Percentage: %.2f%%\n----> Protein Percentage: %.2f%%",
                        fatPercent, carbsPercent, proteinPercent);

                Double sodiumPercentage = (sodium / 2300) * 100; // 2300 = daily recommended intake
                String sodiumBreakdown = String.format(
                        "\n\nTotal Sodium: %.2fmg\nPercentage of Daily Recommended Intake (2,300mg): %.2f%%", sodium,
                        sodiumPercentage);

                String messageString = dl.toString() + totalCalString + totalCalBurnedString + netCaloriesString
                        + nutritionBreakdown + sodiumBreakdown;
                // Provide feedback to the user
                JOptionPane.showMessageDialog(frame, messageString, "DailyLog for that date",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        viewDailyLogChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DailyLog dl = null;
                Date date = null;
                String dateValue = "";
                try {
                    // Prompt the user for a date input
                    JPanel inputPanel = new JPanel();
                    inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

                    JLabel year = new JLabel("Year (YYYY):");
                    JLabel month = new JLabel("Month (MM):");
                    JLabel day = new JLabel("Day (DD):");

                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    inputPanel.add(year);
                    inputPanel.add(yearField);
                    inputPanel.add(month);
                    inputPanel.add(monthField);
                    inputPanel.add(day);
                    inputPanel.add(dayField);
                    try {
                        // Show the input panel in a JOptionPane
                        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "For Which Date?",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Get the values from the input fields
                            String yearValue = yearField.getText();
                            String monthValue = monthField.getText();
                            String dayValue = dayField.getText();
                            dateValue = yearValue + "/" + monthValue + "/" + dayValue;
                            try{
                                date = formatter.parse(dateValue);
                            }
                            catch(Exception f){
                                date = logController.getMostRecentDate();
                            }
                            dl = logController.getDailyLog(date);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    // Handle other exceptions
                    JOptionPane.showMessageDialog(frame, "An error occurred. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                String chartInfoString = logController.getDailyLogChartInfo(dl);
                String[] chartInfo = chartInfoString.split("/");
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                dataset.addValue(Double.parseDouble(chartInfo[0]), "Fats (g)", "Fats (g)");
                dataset.addValue(Double.parseDouble(chartInfo[1]), "Carbohydrates (g)", "Carbohydrates (g)");
                dataset.addValue(Double.parseDouble(chartInfo[2]), "Protein (g)", "Protein (g)");

                String graphTitle = "Daily Log Bar Chart for: " + dateValue;

                JFreeChart chart = ChartFactory.createBarChart(
                        graphTitle,
                        "Nutrition Info",
                        "Grams (g)",
                        dataset);

                CategoryPlot plot = (CategoryPlot) chart.getPlot();

                BarRenderer renderer = (BarRenderer) plot.getRenderer();

                renderer.setSeriesPaint(0, Color.RED);
                renderer.setSeriesPaint(1, Color.GREEN);
                renderer.setSeriesPaint(2, Color.BLUE);

                ChartPanel chartPanel = new ChartPanel(chart);
                JDialog dialog = new JDialog(frame, "Daily Log Bar Chart", true);
                dialog.add(chartPanel);
                dialog.pack();
                dialog.setLocationRelativeTo(frame); // Center the dialog relative to theframe
                dialog.setVisible(true);
            }
        });

        return panel;
    }

    private void centerFrameOnScreen(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        FoodController fc = new FoodController(null);
        ExerciseController ec = new ExerciseController(null);
        LogController lc = new LogController(null, fc, ec);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SwingUI(fc, lc, ec);
            }
        });
    }
}