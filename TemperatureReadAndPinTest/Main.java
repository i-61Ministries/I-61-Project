import java.util.Scanner;
import java.util.ArrayList;
/**
 * Write a description of class Main here.
 * 
 * @author Kevin Sikes
 * @version 10/2/2017
 */
public class Main
{
    private int sleepTime;
    private Device[] devices;
    private Sensor[] sensors;
    private double systemVoltage;
    private ArrayList<String> data;
    //private double

    /**
     * Constructor for objects of class Main
     * Promts user for variables, writes them to a file, and then runs everything else
     */
    public Main(String[] args){
        data = new ArrayList<String>();
        sleepTime = 10000;
        System.out.println("");
        Scanner in = new Scanner (System.in);
        String statement = in.nextLine();
        System.out.println();
        //code in promts and file writer
        System.out.println("Setup complete! Type \'Exit\' and press enter to stop.");
        while(!in.hasNext()){
            run();
            try{
                Thread.sleep(sleepTime);
            }
            catch(Exception e){
                System.out.println("Sleep Time Failure");
                System.out.println(e.getStackTrace());
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
                case "TempAndHum":
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
    private void testActions(){
        
    }
}
