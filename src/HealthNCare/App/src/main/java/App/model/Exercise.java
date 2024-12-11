package App.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class Exercise {
    String exerciseName;
    Double calories;
    static final String FILE = "src/main/resources/exercise.csv";

    public Exercise(String exerciseName, Double calories) {
        this.exerciseName = exerciseName;
        this.calories = calories;
    }

    @Override
    public String toString(){
        String tbReturned = "";

        tbReturned += exerciseName + " ";
        tbReturned += String.valueOf(calories) + " calories";
        return tbReturned;
    }

    public String getName() {
        return exerciseName;
    }

    public Double getCalories() {
        return calories;
    }

    public List<Exercise> readCSV() {
        List<Exercise> exerciseList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE);
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> allData = csvReader.readAll();
            for (int i = 0; i < allData.size(); i++) {
                String[] row = allData.get(i);
                String exerciseName = row[1];
                Double calories = Double.parseDouble(row[2]);
                Exercise exercise = new Exercise(exerciseName, calories);
                exerciseList.add(exercise);

            }
            csvReader.close();
        } catch (Exception e) {
            System.out.println("!!! ERROR !!! ---> " + e);
        }
        return exerciseList;
    }

    public static void main(String[] args) {
        Exercise e = new Exercise("", 0.0);
        List<Exercise> eList = e.readCSV();
        for (Exercise eRow : eList) {
            System.out.println("Exercise Name: " + eRow.getName());
            System.out.println("Exercise Calories: " + eRow.getCalories());
        }

    }
}
