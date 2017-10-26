import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Write a description of class Device here.
 * a device that is either controlled by amount of availible power or a sensor
 * 
 * @author Kevin Sikes
 * @version 10/14/2017
 */
public class Device
{
    Sensor controller;
    public int sensorDataType;
    public boolean sensorControlled;
    public boolean takeActionUp;
    public boolean takeActionLow;
    public float upperActionBound;
    public float lowerActionBound;
    public float criticalPoint;
    public float powerUsage;
    public float lastDataPoint;
    public float currentDataPoint;
    public int pin;
    public boolean deviceState = false;
    final GpioPinDigitalOutput pinOut;
    /**
     * Constructor for objects of class Device
     */
    public Device(boolean sensorControlled, Sensor controller, int sensorDataType, float criticalPoint,boolean takeActionUp, float upperActionBound, boolean takeActionLow, float lowerActionBound, int pin)
    {
        this.sensorControlled = sensorControlled;
        if(!sensorControlled){
            this.controller = null;
            //sensorData type, takeActionUp, takeActionLow, upperActionBound, lowerActionBound, and criticalPoint will not be set
        }
        else{
            this.controller = controller;
            this.criticalPoint = criticalPoint;
            this.takeActionUp = takeActionUp;
            this.takeActionLow = takeActionLow;
            if(!takeActionUp){
                this.upperActionBound = 0f;
            }
            else{
                this.upperActionBound = upperActionBound;
            }
            if(!takeActionLow){
                this.lowerActionBound = 0f;
            }
            else{
                this.lowerActionBound = lowerActionBound;
            }
            this.sensorDataType = sensorDataType;
        }
        this.powerUsage = powerUsage;
        this.pin = pin;
        
        final GpioController gpio = GpioFactory.getInstance();

        switch(pin){
            case 0:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
                break;
            
            case 1:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
                break;
                
            case 2:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
                break;
            
            case 3:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
                break;
            
            case 4:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
                break;
            
            case 5:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);
                break;
            
            case 6:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
                break;
            
            case 7:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.LOW);
                break;
            
            case 8:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, PinState.LOW);
                break;
            
            case 9:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, PinState.LOW);
                break;
            
            case 10:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, PinState.LOW);
                break;
            
            case 11:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, PinState.LOW);
                break;
            
            case 12:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, PinState.LOW);
                break;
            
            case 13:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, PinState.LOW);
                break;
            
            case 14:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, PinState.LOW);
                break;
            
            case 15:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, PinState.LOW);
                break;
            
            case 16:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, PinState.LOW);
                break;
            
            case 17:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, PinState.LOW);
                break;
            
            case 18:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18, PinState.LOW);
                break;
            
            case 19:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19, PinState.LOW);
                break;
            
            case 20:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20, PinState.LOW);
                break;
            
            /**case 21:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW);
                break;
            
            case 22:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, PinState.LOW);
                break;
            
            case 23:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, PinState.LOW);
                break;
            
            case 24:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, PinState.LOW);
                break;
            
            case 25:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, PinState.LOW);
                break;
            
            case 26:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, PinState.LOW);
                break;
            
            case 27:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, PinState.LOW);
                break;
            
            case 28:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, PinState.LOW);
                break;
            
            case 29:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, PinState.LOW);
                break;*/
            default:pinOut = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        }
        
    }
    
    /**
     * -1 means power controlled
     * 0 means no action required;
     * 1 means that upper bound was reached and needs action
     * 2 means that lower bound was reached and needs action
     * 3 means that it is close enough to the critical point to turn off
     */
    public int needsAction(){
        if(sensorControlled){
            lastDataPoint = currentDataPoint;
            currentDataPoint =controller.readData(sensorDataType);
            if(takeActionUp){
                if(currentDataPoint>=upperActionBound){
                    return 1;
                }
            }
            else if(takeActionLow){
                if(currentDataPoint<=lowerActionBound){
                    return 2;
                }
            }
            else{
                if((criticalPoint<currentDataPoint && currentDataPoint<((criticalPoint+upperActionBound)/2)) || (criticalPoint>currentDataPoint && currentDataPoint>((criticalPoint+lowerActionBound)/2))){
                    return 3;
                }
                else{
                    return 0;
                }
            }
        }
        return -1;
    }
    
    public boolean deviceState(){
        return deviceState;
    }
    
    public void turnOn(){
        pinOut.high();
    }
    
    public void turnOff(){
        pinOut.low();
    }
    
    public float getNewCurrentDataPoint(){
        lastDataPoint = currentDataPoint;
        currentDataPoint =controller.readData(sensorDataType);
        return currentDataPoint;
    }
}
