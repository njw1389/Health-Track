package App.model;

import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class BasicFood implements Food {
    String name;
    double calories;
    double fat;
    double carbs;
    double protein;
    double sodium;
    static final String FILE = "src/main/resources/foods.csv";

    public BasicFood(String name, double calories, double fat, double carbs, double protein, double sodium) {
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.carbs = carbs;
        this.protein = protein;
        this.sodium = sodium;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getCalories() {
        return calories;
    }

    @Override
    public double getFat() {
        return fat;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    @Override
    public double getSodium() {
        return sodium;
    }

    public List<BasicFood> readCSV() {
        List<BasicFood> bfList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE);
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> allData = csvReader.readAll();
            for (int i = 0; i < allData.size(); i++) {
                String[] row = allData.get(i);
                String foodType = row[0]; // row[0] = r or b
                if (foodType.equals("b")) {
                    BasicFood bf = new BasicFood(row[1], Double.parseDouble(row[2]), Double.parseDouble(row[3]),
                            Double.parseDouble(row[4]), Double.parseDouble(row[5]), Double.parseDouble(row[6]));
                    bfList.add(bf);
                }
            }
            csvReader.close();
        } catch (Exception e) {
            System.out.println("!!! ERROR !!! ---> " + e);
        }
        return bfList;
    }
}