/*
* Initial Author
*      Michael J. Lutz
*
* Other Contributers
*
* Acknowledgements
*/

/*
* Swing UI class used for displaying the information from the
* associated weather station object.
* This is an extension of JFrame, the outermost container in
* a Swing application.
*/

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.Observer;
import java.util.Observable;
//import java.text.DecimalFormat ;

public class SwingUI extends JFrame implements Observer {
    private JLabel celsiusField; // put current celsius reading here
    private JLabel kelvinField; // put current kelvin reading here
    private JLabel fahrenheitField;
    private JLabel inchesField;
    private JLabel millibarsField;

    // this is the line of code that allows Swing UI to be able to make a weather
    // station inside here and then follow up with it
    private final WeatherStation station;
    // private final Barometer bar;
    /*
     * A Font object contains information on the font to be used to
     * render text.
     */
    private static Font labelFont = new Font(Font.SERIF, Font.PLAIN, 72);

    /*
     * Create and populate the SwingUI JFrame with panels and labels to
     * show the temperatures.
     */
    public SwingUI(WeatherStation station) {
        super("Weather Station");

        // making the SwingUI class into an observer
        this.station = station;
        this.station.addObserver(this);

        // EG - removed Barometer object, gets pressure from WeatherStation

        /*
         * WeatherStation frame is a grid of 1 row by an indefinite
         * number of columns.
         */
        this.setLayout(new GridLayout(1, 0));

        /*
         * There are two panels, one each for Kelvin and Celsius, added to the
         * frame. Each Panel is a 2 row by 1 column grid, with the temperature
         * name in the first row and the temperature itself in the second row.
         */
        JPanel panel;

        /*
         * Set up Kelvin display.
         */
        panel = new JPanel(new GridLayout(2, 1));
        this.add(panel);
        createLabel(" Kelvin ", panel);
        kelvinField = createLabel("", panel);

        /*
         * Set up Celsius display.
         */
        panel = new JPanel(new GridLayout(2, 1));
        this.add(panel);
        createLabel(" Celsius ", panel);
        celsiusField = createLabel("", panel);

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(panel);
        createLabel(" Fahrenheit ", panel);
        fahrenheitField = createLabel("", panel);

        panel = new JPanel(new GridLayout(2, 1));
        this.add(panel);
        createLabel(" Inches ", panel);
        inchesField = createLabel("", panel);

        panel = new JPanel(new GridLayout(2, 1));
        this.add(panel);
        createLabel(" Millibars ", panel);
        millibarsField = createLabel("", panel);

        panel.setPreferredSize(new Dimension(200, 50));
        /*
         * Set up the frame's default close operation pack its elements,
         * and make the frame visible.
         */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

    }

    /*
     * Set the label holding the Kelvin temperature.
     */
    public void setKelvinJLabel(double temperature) {
        kelvinField.setText(String.format("%6.2f", temperature));
    }

    /*
     * Set the label holding the Celsius temperature.
     */
    public void setCelsiusJLabel(double temperature) {
        celsiusField.setText(String.format("%6.2f", temperature));
    }

    public void setFahrenheit(double temp) {
        fahrenheitField.setText(String.format("%6.2f", temp));
    }

    public void setInches(double in) {
        inchesField.setText(String.format("6.2f", in));
    }

    public void setMilli(double milli) {
        millibarsField.setText(String.format("%6.2f", milli));
    }

    /*
     * Create a Label with the initial value <title>, place it in
     * the specified <panel>, and return a reference to the Label
     * in case the caller wants to remember it.
     */
    private JLabel createLabel(String title, JPanel panel) {
        JLabel label = new JLabel(title);

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(labelFont);
        panel.add(label);

        return label;
    }

    @Override
    public void update(Observable obs, Object ignore) {
        /*
         * Check for spurious updates from unrelated objects.
         */
        if (station != obs) {
            return;
        }
        /*
         * Retrieve and print the temperatures.
         */
        // System.out.printf(
        // "Reading is %6.2f degrees C and %6.2f degrees K%n",
        // station.getCelsius(), station.getKelvin()) ;

        // Retieving and displaying the temperatures
        kelvinField.setText(String.format("%6.2f", station.getKelvin()));
        celsiusField.setText(String.format("%6.2f", station.getCelsius()));

        fahrenheitField.setText(String.format("%6.2f", station.getFahrenheit()));
        inchesField.setText(String.format("%6.2f", station.inches())); // EG - changed functions to use station object
        millibarsField.setText(String.format("%6.2f", station.toMillibars(station.inches())));
    }

    public static void main(String[] args) {
        // SwingUI swing = new SwingUI();
        KelvinTempSensorAdaptor sensor = new KelvinTempSensorAdaptor();
        Barometer bar = new Barometer();
        WeatherStation ws = new WeatherStation(sensor, bar); // EG - Injecting sensor objects into WeatherStation

        Thread thread = new Thread(ws);
        thread.start();
        SwingUI swing = new SwingUI(ws);
    }
}