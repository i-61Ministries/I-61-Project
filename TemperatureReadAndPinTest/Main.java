import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
 * Write a description of class Main here.
 * 
 * @author Kevin Sikes
 * @version 10/14/2017
 */
public class Main
{
    private int sleepTime;
    private Device[] devices = new Device[30];
    private boolean[] devicesToCheck = new boolean[30];
    private int numberOfDevices = 0;
    private Sensor[] sensors = new Sensor[30];
    private int numberOfSensors = 0;
    private double systemVoltage;
    private ArrayList<String> data;
    private ArrayList<String> previousData;
    private String[] preDefinedSensorNames = new String[1];
    private boolean firstRun = true;
    private boolean testedActions = false;
    final GpioController gpio = GpioFactory.getInstance();

    /**
     * Constructor for objects of class Main
     * Promts user for variables, writes them to a file, and then runs everything else
     */
    public Main(String[] args){
        /**
        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        GpioUtil.export(3, GpioUtil.DIRECTION_OUT);*/
        
        //setting up pre-defined sensor names
        preDefinedSensorNames[0] = "dht11";
        //initializing variables
        boolean exit = false;
        data = new ArrayList<String>();
        previousData = new ArrayList<String>();
        sleepTime = 10000;
        System.out.println("Staring Setup");
        Scanner in = new Scanner (System.in);
        String statement;
        int sensorPromts;
        while(true){
            System.out.println("How many sensors will you use? Ex: 2");
            statement = in.nextLine();
            try{
                if(Integer.parseInt(statement) >0){
                    sensorPromts = Integer.parseInt(statement);
                    break;
                }
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number > 0");
            }
        }
        
        
        //code in promts and file writer
        for(int x = 0;x<sensorPromts;x++){
            sensorPrompt(in);
        }
        
        while(true){
            System.out.println("How often (in minutes) do you want all of the sensors to take a reading? \n (If there are multiple times that you want, pick the smallest time)\n Ex: 2");
            statement = in.nextLine();
            try{
                if(Integer.parseInt(statement) >0){
                    sleepTime = Integer.parseInt(statement)*60*1000;
                    break;
                }
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number > 0");
            }
        }
        
        while(true){
            System.out.println("Do you want to add a device to the system? (yes/no)");
            statement = in.nextLine();
            if(statement.toLowerCase().equals("yes")){
                devicePrompt(in);
            }
            else if (statement.toLowerCase().equals("no")){
                break;
            }
            else{
                System.out.println("Please answer with \'yes\' or \'no\'");
            }
        }
        
        System.out.println("Setup complete!");
        while(!exit){
            run();
            if(testedActions){
                try{
                    Thread.sleep(sleepTime/2);
                }
                catch(Exception e){
                    System.out.println("Sleep Time Failure");
                    System.out.println(e.getStackTrace());
                }
            }
            else{
                try{
                    Thread.sleep(sleepTime);
                }
                catch(Exception e){
                    System.out.println("Sleep Time Failure");
                    System.out.println(e.getStackTrace());
                }
            }
            /**if(in.hasNext()){
                if(in.nextLine().toLowerCase() == "exit"){
                    exit=true;
                }
            }*/
          }
    }
    
    private void sensorPrompt(Scanner in){
        String statement;
        while(true){
            System.out.println("Which sensor will you be using? (type the number by the sensor type)");
            int x = 0;
            for(String s:preDefinedSensorNames){
                if(s != null){
                    System.out.println(x + ": " + s);
                }
                x++;
            }
            statement = in.nextLine();
            try{
                boolean notASensor = false;
                switch(Integer.parseInt(statement)){
                    case 0:
                        int[] typeAry = {0,1};
                        String[] typeNameAry = {"Humidity","Temperature"};
                        int pin;
                        while(true){
                            System.out.println("What GPIO pin will the sensor use? (EX: 21)");
                            statement = in.nextLine();
                            try{
                                pin = Integer.parseInt(statement);
                                break;
                            }
                            catch(Exception e){
                                System.out.println("Please answer with a valid number");
                            }
                        }
                        sensors[numberOfSensors] = new TemperatureTest(32.0f,122.0f,typeAry ,typeNameAry,pin);
                        numberOfSensors++;
                        break;
                    default: System.out.println("Please answer with a valid number");
                        notASensor=true;
                        break;
                }
                if(notASensor){
                    System.out.println("Please answer with a vaild integer");
                }
                else{
                    break;
                }
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number");
            }
        }
    }
    
    /**
     * prompt devices only after ALL sensors have been declared
     */
    private void devicePrompt(Scanner in){
        String statement;
        Device d;
        Sensor controller = null;
        int sensorDataType = 0;
        boolean sensorControlled;
        boolean takeActionUp = false;
        boolean takeActionLow = false;
        float upperActionBound = 0.0f;
        float lowerActionBound = 0.0f;
        float criticalPoint = 0.0f;
        float powerUsage = 0.0f;
        int pin = 0;
        while(true){
            System.out.println("Will this device be controlled by a sensor?(Yes/No)");
            statement = in.nextLine().toLowerCase();
            if(statement.equals("yes")){
                sensorControlled = true;
                break;
            }
            else if(statement.equals("no")){
                sensorControlled = false;
                break;
            }
            else{
                System.out.println("Please answer with \'Yes\' or \'No\'");
            }
        }
        while(sensorControlled){
            System.out.println("Which sensor will control the device?(type the number of the sensor from the given list)");
            for(int x = 0;x<sensors.length-1;x++){
                if(sensors[x] != null){
                    System.out.println(x + ":" + sensors[x].getName());
                }
            }
            statement = in.nextLine();
            try{
                if(sensors[Integer.parseInt(statement)] !=null){
                    controller = sensors[Integer.parseInt(statement)];
                    while(controller.types.length>1){
                        System.out.println("What data type will the device use?");
                        for(int x = 0;x<controller.types.length;x++){
                            if(sensors[x] != null){
                                System.out.println(x + ":" + controller.typesName[x]);
                            }
                        }
                        statement = in.nextLine();
                        try{
                            if(Integer.parseInt(statement)<controller.types.length&&Integer.parseInt(statement)>=0){
                                sensorDataType = controller.types[Integer.parseInt(statement)];
                                break;
                            }
                            else{
                                System.out.println("Please pick one of the data types shown");
                            }
                        }
                        catch(Exception e){
                            System.out.println("Please answer with a valid number");
                        }
                    }
                    while(true){
                        System.out.println("Does the device need to react if the data from the sensor is above a certain value? (yes/no)");
                        statement = in.nextLine();
                        if(statement.toLowerCase().equals("yes")){
                            takeActionUp=true;
                            while(true){
                                System.out.println("What is the value? EX: 74.3");
                                statement = in.nextLine();
                                try{
                                    upperActionBound = Float.parseFloat(statement);
                                    break;
                                }
                                catch(Exception e){
                                    System.out.println("Please answer with a valid number containing a decimal");
                                }
                            }
                            break;
                        }
                        else if(statement.toLowerCase().equals("no")){
                            takeActionUp=false;
                            break;
                        }
                        else{
                            System.out.println("Please answer with \'yes\' or \'no\'");
                        }
                    }
                    while(true){
                        System.out.println("Does the device need to react if the data from the sensor is below a certain value? (yes/no)");
                        statement = in.nextLine();
                        if(statement.toLowerCase().equals("yes")){
                            takeActionLow=true;
                            while(true){
                                System.out.println("What is the value? EX: 73.2");
                                statement = in.nextLine();
                                try{
                                    lowerActionBound = Float.parseFloat(statement);/**could search to see if there iss a decimal and if not add it myself*/
                                    break;
                                }
                                catch(Exception e){
                                    System.out.println("Please answer with a valid number containing a decimal");
                                }
                            }
                            break;
                        }
                        else if(statement.toLowerCase().equals("no")){
                            takeActionLow=false;
                            break;
                        }
                        else{
                            System.out.println("Please answer with \'yes\' or \'no\'");
                        }
                    }
                    while(true){
                        System.out.println("What sensor value is best for the device to turn off at? \n EX: An air conditioner set to turn off when a room cools to 75.0 degrees farenheight");
                        statement = in.nextLine();
                        try{
                            criticalPoint = Float.parseFloat(statement);
                            break;
                        }
                        catch(Exception e){
                            System.out.println("Please answer with a valid number containing a decimal");
                        }
                    }
                    break;
                }
                else{
                    System.out.println("Please pick one of the sensors shown");
                }
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number");
            }
        }
        while(true){
            System.out.println("How much power does the device use in Amps/Hour? EX: 3.1");
            statement = in.nextLine();
            try{
                powerUsage = Float.parseFloat(statement);
                break;
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number containing a decimal");
            }
        }
        while(true){
            System.out.println("What GPIO pin will the sensor use? (EX: 21)");
            statement = in.nextLine();
            try{
                pin = Integer.parseInt(statement);
                break;
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number");
            }
        }
        d = new Device(sensorControlled,controller,sensorDataType,criticalPoint,takeActionUp,upperActionBound,takeActionLow,lowerActionBound,pin,gpio);
        for(int x = 0;x<devices.length-1;x++){
            if(devices[x] == null){
                devices[x] = d;
                break;
            }
        }
    }
    
    /**
     * 
     */
    public Main(boolean restoreFromFile){
        
    }
    
    /**
     * 
     */
    private void run(){
        getSensorData();
        controlLogic();
        testActions();
    }
    
    /**
     * 
     */
    private void getSensorData(){
        if(!firstRun){
            previousData = data;
            data = new ArrayList<String>();
        }
        else{
            firstRun = false;
        }
        for(Sensor s:sensors){
            if(s != null){
                switch(s.getName()){
                    case "dht11":
                        data.add("Humidity:" + s.readData(1));
                        data.add("Temperature:" + s.readData(0));
                        break;
                        //need to add cases for other sensors
                    }
                }
            else{
                break;
            }
        }
        try{
            FileWriter writer;
            File file = new File("/home/pi/Desktop/SensorData.txt");
            writer = new FileWriter(file,true);
            for(String s:data){
                if(!s.equals("")){
                    writer.append(s);
                    System.out.println(s);
                    writer.append("\n");
                }
            }
            writer.append("\n");
            writer.flush();
            writer.close();
        }
        catch(IOException e){
            System.out.println("Data write error");
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    private void controlLogic(){
        int action;
        int powerAction = predictEnergyConsumptionAndAction();
        int deviceNum = 0;
        for(Device d:devices){
            if(d!=null){
                action = d.needsAction();
                if(action == 1){//upper action
                    d.turnOn();
                    devicesToCheck[deviceNum] = true;
                }
                else if (action == 2){//lower action
                    d.turnOn();
                    devicesToCheck[deviceNum] = true;
                }
                else if (action == -1){//action determined by power
                    if(powerAction == 1){
                        d.turnOff();
                        devicesToCheck[deviceNum] = true;
                    }
                    else{
                        d.turnOn();
                        devicesToCheck[deviceNum] = false;
                    }
                }
                else if (action == 3){
                    d.turnOff();
                    devicesToCheck[deviceNum] = false;
                }
                deviceNum++;
            }
            else{
                break;
            }
        }
    }
    
    /**
     * return 0 if ok on power
     * return 1 if power consumption is too high
     */
    private int predictEnergyConsumptionAndAction(){
        return 0;
    }
    
    /**
     * compares the last data point with a more current one to the critical point
     * This will warn the user if the current data point is farther than the last data point from the critical point
     */
    private void testActions(){
        boolean needToTestActions = false;
        testedActions = false;
        for(boolean b:devicesToCheck){
            if(b){
                needToTestActions = true;
            }
        }
        if(needToTestActions){
            testedActions = true;
            try{
                Thread.sleep(sleepTime/2);
            }
            catch(Exception e){
                System.out.println("Sleep Time Failure");
                System.out.println(e.getStackTrace());
            }
            for(int i = 0;i<devices.length;i++){
                if(devicesToCheck[i]){
                    float lastDataPoint = devices[i].lastDataPoint;
                    float currentDataPoint = devices[i].getNewCurrentDataPoint();
                    float criticalPoint = devices[i].criticalPoint;
                    if(Math.abs(criticalPoint-lastDataPoint) > Math.abs(criticalPoint-currentDataPoint)){
                        System.out.println("The value that device " + i + "on pin " + devices[i].pin + " depends on is farther away from the critical point than the last value: \n last value: " + lastDataPoint + " Current value: " + currentDataPoint + " Critical Point: " + criticalPoint);
                    }
                }
            }
        }
    }
}
