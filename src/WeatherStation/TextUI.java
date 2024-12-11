/*
 * Initial Author
 *      Michael J. Lutz
 *
 * Other Contributers
 * 
 * Acknowledgements
 */
 
/*
 * The TextUI class is an observer of the WeatherStation that,
 * when it receives an update message, prints the temperature
 * in Celsius and Kelvin.
 *
 * The main method for the text based monitoring application
 * is here as well.
 */
import java.util.Observer ;
import java.util.Observable ;

public class TextUI implements Observer {
    private final WeatherStation station ;
    private final Barometer bar;

    /*
     * Remember the station we're attached to and
     * add ourselves as an observer.
     */
    public TextUI(WeatherStation station, Barometer barometer) {
        this.station = station ;
        this.station.addObserver(this) ;

        this.bar = barometer;
        this.bar.addObserver(this);
    }

    /*
     * Called when WeatherStation gets another reading.
     * The Observable should be the station; the Object
     * argument is ignored.
     */
    public void update(Observable obs, Object ignore) {
        /*
         * Check for spurious updates from unrelated objects.
         */
        if( station != obs ) {
            return ;
        }
        /*
         * Retrieve and print the temperatures.
         */
        System.out.printf(
                "Temperature:   %6.2f C  %6.2f F  %6.2f K  \n",
                station.getCelsius(), station.getFahrenheit(), station.getKelvin()) ;

        System.out.printf("Pressure:      %6.2f inches   %6.2f mbar \n", bar.pressure(), bar.toMillibars(bar.pressure()));
    }

    /*
     * Start the application.
     */
    public static void main(String[] args) {
        KelvinTempSensorAdaptor tempSensor = new KelvinTempSensorAdaptor();
        Barometer barometer = new Barometer();
        WeatherStation ws = new WeatherStation(tempSensor, barometer);
        TextUI ui = new TextUI(ws, barometer);
        Thread thread = new Thread(ws);
        thread.start();
    }    
}