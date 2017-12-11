import java.util.concurrent.TimeUnit;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
/**
 * osepp volt-1 voltage sensor reader
 * 
 * @author Kevin Sikes 
 * @version 12/11/2017
 */
public class VoltageSensor extends Sensor
{
    private float voltage;
    
    public String name;
    public float upperBound;//Sensor's physical capibilities
    public float lowerBound;
    public int[] types;
    public String[] typesName;
    public int pin;
    
    /**
     * Constructor for objects of class Sensor
     */
    public VoltageSensor(float upperBound, float lowerBound, int[] types, String[]typesName, int pin)
    {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.types = types;
        this.typesName = typesName;
        this.pin = pin;
    }

    /**
     * This module is based on the principle of the resistor divider design, enabling the terminal interface 
     * input voltage reduced five times,  analog input voltage up to 5V, then the voltage detection module 
     * can not be greater than the input voltage of 5V × 5 = 25V (3.3V if used system, the input voltage can not
     * exceed 3.3Vx5 = 16.5V). Because  AVR chips used in 10 AD, so this module's analog resolution 0.00489V (5V/1023),
     * so the input voltage detection module detects a minimum voltage of 0.00489V × 5 = 0.02445V.
     */
    public float readData(int type){
        /*int voltageVal;
        int calcVoltageVal;
        int LED1 = 13;
 

        pinMode (LED1, OUTPUT);
        Serial.begin (9600);


        float temp;
        voltageVal = analogRead (0); //read the value from voltage sensor.
        temp = voltageVal/4.092; //resolution value to store
        voltageVal = (int) temp ;
        calcVoltageVal = ((voltageVal% 100) / 10); //calculate stepped down voltage
        Serial.println ("Voltage: ");
        Serial.println (calcVoltageVal); //print out voltage readings.
        delay (1000);*/

        float voltageVal;
        float calcVoltageVal;
 

        Gpio.pinMode (LED1, Gpio.INPUT);


        float temp;
        voltageVal = analogRead (pin); //read the value from voltage sensor.
        temp = voltageVal/4.092; //resolution value to store
        voltageVal = temp ;
        calcVoltageVal = ((voltageVal% 100) / 10); //calculate stepped down voltage
        Serial.println ("Voltage: ");
        Serial.println (calcVoltageVal); //print out voltage readings.
        delay (1000);
        /*http://www.instructables.com/id/Arduino-Voltage-Sensor-0-25V/
         * https://www.osepp.com/electronic-modules/sensor-modules/81-voltage-sensor-module
         * https://www.alliedelec.com/osepp-volt-01/70669203/
         * https://gist.github.com/jean-robert/4055869
         * 
         * 
         */
    }
    
    /**
     * 
     */
    public String getName(){
        return name;
    }
}
