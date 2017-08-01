package com.i61;
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
 * This will read the teamperature of the dht11 for the raspberry [pi
 * https://stackoverflow.com/questions/28486159/read-temperature-from-dht11-using-pi4j
 * @author Eric Smith and yglodt and modified by Kevin Sikes
 * @version 10/14/2017
 */
public class TemperatureTest extends Sensor
{
    private static final int    MAXTIMINGS  = 85;
    private final int[]         dht11_dat   = { 0, 0, 0, 0, 0 };
    private float humidity;
    private float temperature;
    
    public String name = "dht11";
    public float upperBound;
    public float lowerBound;
    public int[] types;
    public String[] typesName;
    public int pin;

    /**
     * Constructor for objects of class TemperatureTest
     */
    public TemperatureTest(float upperBound, float lowerBound, int[] types, String[] typesName, int pin) {
        super(upperBound,lowerBound,types,typesName, pin);
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        this.types = types;
        this.typesName = typesName;
        this.pin = pin;
        /**
        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        GpioUtil.export(3, GpioUtil.DIRECTION_OUT);*/
    }
    
    public void getTemp() throws Exception{
        int timesRun = 0;
        while(!getTemperature(pin)){
            Thread.sleep(2000);
            timesRun++;
            if(timesRun>20){
                System.out.println("A dht11 sensor on pin "+ pin +" failed to get a reading and may be malfunctioning");
                break;
            }
        }
    }
    
    public String getName(){
        return name;
    }

    private boolean getTemperature(final int pin) {
        int laststate = Gpio.HIGH;
        int j = 0;
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;

        Gpio.pinMode(pin, Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.LOW);
        Gpio.delay(18);

        Gpio.digitalWrite(pin, Gpio.HIGH);
        Gpio.pinMode(pin, Gpio.INPUT);

        for (int i = 0; i < MAXTIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(pin) == laststate) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            laststate = Gpio.digitalRead(pin);

            if (counter == 255) {
                break;
            }

            /* ignore first 3 transitions */
            if (i >= 4 && i % 2 == 0) {
                /* shove each bit into the storage bytes */
                dht11_dat[j / 8] <<= 1;
                if (counter > 16) {
                    dht11_dat[j / 8] |= 1;
                }
                j++;
            }
        }
        // check we read 40 bits (8bit x 5 ) + verify checksum in the last
        // byte
        if (j >= 40 && checkParity()) {
            float h = (float) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if (h > 100) {
                h = dht11_dat[0]; // for DHT11
            }
            float c = (float) (((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if (c > 125) {
                c = dht11_dat[2]; // for DHT11
            }
            if ((dht11_dat[2] & 0x80) != 0) {
                c = -c;
            }
            final float f = c * 1.8f + 32;
            //System.out.println("Humidity = " + h + " Temperature = " + c + "(" + f + "f)");
            humidity = h;
            temperature = f;
            return true;
        } else {
            //System.out.println("Data not good, skip");
            return false;
        }

    }

    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }

//    public static void main(final String ars[]) throws Exception {
//
//        final TemperatureTest dht = new TemperatureTest();
//
//        /**for (int i = 0; i < 10; i++) {
//            Thread.sleep(2000);
//            dht.getTemperature(21);
//        }*/
//
//        while(!dht.getTemperature(21)){
//            Thread.sleep(2000);
//            //dht.getTemperature(21);
//        }
//
//        //System.out.println("Done!!");
//
//    }


    @Override
    public float readData(int type){

        try{
            getTemp();
        }
        catch(Exception e){
            System.out.println("Read Data Error for sensor on pin " + pin);
            System.out.println(e.getStackTrace());
        }

        if(type==0){
            return humidity;
        }
        else{
            return temperature;
        }
    }

}
