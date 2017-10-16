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
 * Write a description of class Sensor here.
 * abstract class for generic sensor properties
 * 
 * @author Kevin Sikes 
 * @version 10/14/2017
 */
public abstract class Sensor
{
    public String name;
    public float upperBound;//Sensor's physical capibilities
    public float lowerBound;
    public int[] types;
    public String[] typesName;
    public int pin;
    
    /**
     * Constructor for objects of class Sensor
     */
    public Sensor(float upperBound, float lowerBound, int[] types, String[]typesName, int pin)
    {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.types = types;
        this.typesName = typesName;
        this.pin = pin;
    }

    public abstract float readData(int type);
    
    public abstract String getName();
    
}
