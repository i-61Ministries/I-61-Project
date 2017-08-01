import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * This will read the teamperature of the dht11 for the raspberry [pi
 * https://stackoverflow.com/questions/28486159/read-temperature-from-dht11-using-pi4j
 * @author Kevin Sikes 
 * @version 9/6/2017
 */
public class TemperatureTest
{
    #define MAXTIMINGS  85
    #define DHTPIN      15  //DHT connect to TxD
    int dht11_dat[5] ={0,0,0,0,0};//store DHT11 data

    /**
     * Constructor for objects of class TemperatureTest
     */
    public TemperatureTest()
    {
        
    }

    /**
     * 
     */
    public int sampleMethod()
    {
        
    }
}
