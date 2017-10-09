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
    private Sensor[] sensors = new Sensor[30];
    private double systemVoltage;
    private ArrayList<String> data;
    //private double

    /**
     * Constructor for objects of class Main
     * Promts user for variables, writes them to a file, and then runs everything else
     */
    public Main(String[] args){
        boolean exit = false;
        data = new ArrayList<String>();
        sleepTime = 10000;
        System.out.println("");
        Scanner in = new Scanner (System.in);
        String statement = in.nextLine();
        System.out.println();
        //code in promts and file writer
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
                if(in.nextLine() == "Exit"){
                    exit=true;
                }
            }
          }
    }
    
    private void sensorPrompt(Scanner in){
        
    }
    
    /**
     * prompt devices only after ALL sensors have been declared
     */
    private void devicePrompt(Scanner in){
        String statement;
        Device d;
        Sensor controller;
        int sensorDataType;
        boolean sensorControlled;
        boolean takeActionUp;
        boolean takeActionLow;
        float upperActionBound;
        float lowerActionBound;
        float criticalPoint;
        float powerUsage;
        while(true){
            System.out.println("Will this device be controlled by a sensor?(Yes/No)");
            statement = in.nextLine().toLowerCase();
            if(statement == "yes"){
                sensorControlled = true;
                break;
            }
            else if(statement == "no"){
                sensorControlled = false;
                break;
            }
            else{
                System.out.println("Please answer with \'Yes\' or \'No\'");
            }
        }
        while(sensorControlled){
            System.out.println("Which sensor will control the device?(type the number of the sensor from the given list)");
            for(int x = 0;0<sensors.length;x++){
                if(sensors[x] != null){
                    System.out.println(x + ":" + sensors[x].getName());
                }
            }
            statement = in.nextLine();
            try{
                if(sensors[Integer.parseInt(statement)] !=null){
                    controller = sensors[Integer.parseInt(statement)];
                    break;
                }
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number");
            }
        }
        /**while(controller.types.length>1){
            System.out.println("What data type will the device use?");
            for(int x = 0;0<controller.types.length;x++){
                if(sensors[x] != null){
                    System.out.println(x + ":" + controller.typesName[x]);
                }
            }
            statement = in.nextLine();
            try{
                if(Integer.parseInt(statement)>=controller.types.length){
                    sensorDataType = controller.types[Integer.parseInt(statement)];
                    break;
                }
            }
            catch(Exception e){
                System.out.println("Please answer with a valid number");
            }
        }*/
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
        
    }
    
    /**
     * 
     */
    private void takeAction(){
        
    }
    
    /**
     * 
     */
    private void predictEnergyConsumptionAndAction(){
        
    }
    
    /**
     * 
     */
    private void testActions(){
        
    }
}
