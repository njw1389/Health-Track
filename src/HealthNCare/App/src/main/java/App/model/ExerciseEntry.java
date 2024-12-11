package App.model;

import App.controller.ExerciseController;

public class ExerciseEntry {
    public Exercise exercise;
    public Double minutes;
    public ExerciseController ec = new ExerciseController(null);

    public ExerciseEntry(Exercise exercise, Double minutes) {
        this.exercise = exercise;
        this.minutes = minutes;
    }

    public ExerciseEntry() {
        this.exercise = null;
        this.minutes = 0.0;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Double getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return minutes + " minutes of " + exercise + ".";
    }

    public String toExerciseEntryString(double weight) {
        String fe = "";
        double totalCalBurned = ec.calculateCaloriesExpended(exercise.getName(), weight, minutes);
        fe += "\n\n--> Exercise: " + exercise.getName() + " ";
        fe += "\n\t----> Minutes: " + minutes;
        String formattedString = String.format("\n\t----> Total Cals Burned: %.2f", totalCalBurned);
        fe += formattedString;

        return fe;
    }
}
