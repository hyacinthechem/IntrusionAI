
package src;

import java.lang.annotation.ElementType;
import java.util.*;
import java.io.*;
import ecs100.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;


public class IntrusionDetector extends JFrame {

    private boolean administrator;
    private boolean portRisk = false;
    private double infectedPorts;
    private String adminPassword;
    private String filePath = "data/sshd.xlsx";
    private NetworkData main;
    public static final int MAXIMUM_FAILURES = 20;

    private  Map<String,List<Cell>> networkMap;
    private  List<Feature<?>> allFeatures;
    private  List<Double> portNumbers;
    private Map<String, Feature<?>> featureMap = new HashMap<>();



    public void UserInterface(){
        UI.addButton("Login" , this::doLogin);
        UI.addButton("Latest Detections ", this::detections);
        UI.addButton("BlackList Port", this::blackListPort);
        UI.addButton("Print Log File", this::printMap);
        UI.addButton("Debug", this::debugger);

    }

    private void debugger(){
        main.loadAllRequests();
    }

    private void loadPassword(){
        Properties prop = new Properties();
        try(InputStream input = new FileInputStream("data/config.properties")){
            prop.load(input);
            adminPassword = prop.getProperty("adminPassword");

         }catch(IOException e){
         UI.println("Error loading admin password");
        }
    }

    public void doLogin(){
        String password = UI.askString("Enter Password");
        if(password.equals(adminPassword)){
            administrator = true;
        }else{
            administrator = false;
            UI.println("Incorrect Password");
        }
    }



    public void detections(){
        if(administrator){
            acceptedFailed();
            portScanner();

        }else{
            UI.println("Please Sign in as Administrator");
        }
    }

    public void portScanner(){
        for(String feature : featureMap.keySet()){
            if(feature.equals("PORT")){
                Feature<?> featureObj = featureMap.get(feature);
                List<?> featureList = featureObj.getFeaturesList();
                for(Object o : featureList){
                    if(o instanceof Port){
                        Port port = (Port)o;
                        if(portNumbers.contains(port.getPortNumber())){
                            infectedPorts++;
                        }
                    }
                }
            }
        }
        UI.println("Detected: " + infectedPorts + " Infected Ports");
    }

    public void acceptedFailed(){
        int count = 0;
        List<Cell> cells = new ArrayList<>();
        for(String featureName : networkMap.keySet()){
            if(featureName.equals("ACCEPTED-FAILED")){
              cells = networkMap.get(featureName);
            }
        }

        for(Cell cell : cells){
            if(cell.getStringCellValue().equals("Failed")){
                count++;
            }
        }

        if(count>MAXIMUM_FAILURES){
            UI.println("Count: " + count + "  System shut down");
        }

    }

    public void printMap(){
        for(Map.Entry<String,List<Cell>> entry : networkMap.entrySet()){
            String row = entry.getKey();
            List<Cell> cells = entry.getValue();
            UI.println(row);
            UI.println(cells);
        }

    }



    public void blackListPort(){
        portNumbers = UI.askNumbers("Ports: ");
        if(!portNumbers.isEmpty()){
            portRisk = true;
        }
    }

    public void NetworkDataLoader(){
        main = new NetworkData(filePath); //initialise the NetworkData object
        main.loaders(); //load the xlsx file data and put into networkMap
        allFeatures = main.featureBuild(); //extract featureObjects from built networkMap
        main.loadAllRequests();
        networkMap = main.getLogFileMap(); //Get copy of networkMap for IntrusionDetector class
    }

    /* Feature Map must have rowHeaders "PORT", "DATE" "TIME" ETC as the Key,
    and then the list of the features being the columns as the Pair Value*

    So then you have size 14 and then the list of all the columns for that feature
     */

    public void constructFeatureMap(){

        for(Feature<?> feature : allFeatures){
            featureMap.put(feature.toString(), feature);
        }

    }


    public static void main(String[] args){
        IntrusionDetector id = new IntrusionDetector();
        id.loadPassword();
        id.NetworkDataLoader();
        id.constructFeatureMap();
        id.UserInterface();
    }
}


/*
* Once requestMap is properly built where usernames point to a list of all the requests theyve made then we can continue to
* "AI" methods. this will be an attempt at creating nodes of "requests" that contain the features like IPADDRESS USERTYPE ETC.
*  These methods will have mathematical operations to decide whether a request is anomalous or not anomalous based on predetermined values
* The aim from there is to create new unseen data of requests where a classification task decides whether it is anomalous or not.
*
*
* */