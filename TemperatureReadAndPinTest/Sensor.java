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
 * 
 * @author Kevin Sikes 
 * @version 10/2/2017
 */
public abstract class Sensor
{
    public String name;
    public boolean takeActionUp;
    public boolean takeActionLow;
    public boolean uBound;
    public boolean lBound;
    public float upperBound;
    public float lowerBound;
    public double midPoint;
    public Device actionUp;
    public Device actionLow;
    public int[] types;
    
    /**
     * Constructor for objects of class Sensor
     */
    public Sensor(boolean takeActionUp, boolean takeActionLow, boolean uBound, boolean lBound, float upperBound, float lowerBound,Device action, int[] types, Device actionLow)
    {
        this.takeActionUp = takeActionUp;
        this.takeActionLow = takeActionLow;
        this.uBound = uBound;
        this.lBound = lBound;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.actionUp = actionUp;
        this.actionLow = actionLow;
        this.types = types;
        if(uBound&&lBound){
            setMid();/**add good point?*/
        }
    }
    
    private void setMid(){
        midPoint = (upperBound+lowerBound)/2;
    }

    public abstract float readData(int type);
    
    public String getName(){
        return name;
    }
    
}
