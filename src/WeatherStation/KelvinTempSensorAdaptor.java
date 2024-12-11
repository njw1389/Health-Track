import java.util.Random;

// This was the implementation made in class and was mostly okayed by the TA.
// Still need to know if we need to do injection here like in WeatherStation
// -- Allan
public class KelvinTempSensorAdaptor implements ITempSensor {
    private KelvinTempSensor sensor;

    public KelvinTempSensorAdaptor() {
        sensor = new KelvinTempSensor();
    }

    public int reading() {
        return sensor.reading();
    }
}
