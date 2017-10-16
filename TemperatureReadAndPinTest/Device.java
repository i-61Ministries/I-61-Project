
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
    public int pin;

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
                //upperActionBound will not be set
            }
            else{
                this.upperActionBound = upperActionBound;
            }
            if(!takeActionLow){
                //lowerActionBound will not be set
            }
            else{
                this.lowerActionBound = lowerActionBound;
            }
            this.sensorDataType = sensorDataType;
        }
        this.powerUsage = powerUsage;
        this.pin = pin;
    }
    
    /**
     * -1 means power controlled
     * 0 means no action required;
     * 1 means that upper bound was reached and needs action
     * 2 means that lower bound was reached and needs action
     */
    public int needsAction(){
        float data;
        if(sensorControlled){
            data =controller.readData(sensorDataType);
            if(takeActionUp){
                if(data>=upperActionBound){
                    return 1;
                }
            }
            else if(takeActionLow){
                if(data<=lowerActionBound){
                    return 2;
                }
            }
            else{
                return 0;
            }
        }
        return -1;
    }
}
