import java.util.Scanner;
import java.util.ArrayList;
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
    private int numberOfDevices = 0;
    private Sensor[] sensors = new Sensor[30];
    private int numberOfSensors = 0;
    private double systemVoltage;
    private ArrayList<String> data;
    private String[] preDefinedSensorNames = new String[1];

    /**
     * Constructor for objects of class Main
     * Promts user for variables, writes them to a file, and then runs everything else
     */
    public Main(String[] args){
        //setting up pre-defined sensor names
        preDefinedSensorNames[0] = "dht11";
        //initializing variables
        boolean exit = false;
        data = new ArrayList<String>();
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
        
        System.out.println("Setup complete! Type \'Exit\' and press enter to stop.");
        while(!exit){
            run();
            try{
                Thread.sleep(sleepTime);
            }
            catch(Exception e){
                System.out.println("Sleep Time Failure");
                System.out.println(e.getStackTrace());
            }
            if(in.hasNext()){
                if(in.nextLine().toLowerCase() == "exit"){
                    exit=true;
                }
            }
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
                if(!notASensor){
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
                            criticalPoint = Float.parseFloat(statement);/**could search to see if there is a decimal and if not add it myself*/
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
                powerUsage = Float.parseFloat(statement);/**could search to see if there is a decimal and if not add it myself*/
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
        d = new Device(sensorControlled,controller,sensorDataType,criticalPoint,takeActionUp,upperActionBound,takeActionLow,lowerActionBound,pin);
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
        takeAction();
        testActions();
    }
    
    /**
     * 
     */
    private void getSensorData(){
        for(Sensor s:sensors){
            switch(s.getName()){
                case "dht11":
                        data.add("Humidity:" + s.readData(1));
                        data.add("Temperature:" + s.readData(0));
                        break;
            }
        }
    }
    
    /**
     * 
     */
    private void controlLogic(){
        int action;
        for(Device d:devices){
            action = d.needsAction();
            if(action == 1){
                
            }
            else if (action == 2){
                
            }
            else if (action == -1){
                
            }
        }
    }
    
    /**
     * 
     */
    private void takeAction(){
        
    }
    
    /**
     * 
     */
    private int predictEnergyConsumptionAndAction(){
        return 0;
    }
    
    /**
     * 
     */
    private void testActions(){
        
    }
}
