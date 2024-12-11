package App.controller;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import App.model.Exercise;
import App.view.UserInterface;

public class ExerciseController {
    Exercise exercise = new Exercise("", 0.0);
    List<Exercise> exerciseList = new ArrayList<>();
    UserInterface view;
    static final String FILE = "src/main/resources/exercise.csv";

    public ExerciseController(UserInterface view) {
        this.view = view;
    }

    public void init() {
        exerciseList = exercise.readCSV();
    }

   

    public int addExercise(String exerciseName, Double calories) {
        List<Exercise> exerciseList = exercise.readCSV();
        boolean duplicate = false;
        for (Exercise exercise : exerciseList) {
            if (exerciseName.equals(exercise.getName())) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            try {
                FileWriter fileWriter = new FileWriter(FILE, true);
                CSVWriter csvWriter = new CSVWriter(fileWriter,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
                String[] data = { "e", exerciseName, calories.toString() };
                csvWriter.writeNext(data);
                csvWriter.close();
                return 0; // return 0 to indicate program adding success
            } catch (Exception e) {
                System.out.println("!!! ERROR !!! ---> " + e);
                return 1; // return 1 to indicate failure
            }
        } else {
            System.out.println("!!! ERROR !!! ---> Duplicate Detected!");
            return 1; // return 1 to indicate failure
        }

    }

    public int removeExercise(String removeName) {
        try {
            FileReader fileReader = new FileReader(FILE);
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> allData = csvReader.readAll();
            for (int i = 0; i < allData.size(); i++) {
                String[] row = allData.get(i);
                String exerciseName = row[1]; // row[1] will always be name of food
                if (removeName.equals(exerciseName)) {
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
            System.out.println("!!! ERROR !!! ---> " + e);
            return 1; // indication of failure
        }
    }

    public Double calculateCaloriesExpended(String exerciseName, Double weight, Double minutes) {
        List<Exercise> exerciseList = exercise.readCSV();
        Double calExpended = 0.0;
        for (Exercise exercise : exerciseList) {
            if (exerciseName.equals(exercise.getName())) {
                calExpended = exercise.getCalories() * (weight / 100.0) * (minutes / 60.0);
            }
        }
        return calExpended;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList = exercise.readCSV();
    }

    public static void main(String[] args) {
        ExerciseController ec = new ExerciseController(null);
        // System.out.println(ec.calculateCaloriesExpended("Gardening", 160.0, 15.0));
        // ec.addExercise("Kayaking", 292.1);
        // ec.removeExercise("Kayaking");
    }
}
