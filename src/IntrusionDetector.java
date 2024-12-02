
package src;

import java.lang.annotation.ElementType;
import java.util.*;
import java.io.*;
import ecs100.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;


public class IntrusionDetector extends JFrame {

    private boolean administrator;
    private String adminPassword;
    private String filePath = "data/sshd.xlsx";
    private NetworkData main;
    public static final int MAXIMUM_FAILURES = 20;

    private  Map<String,List<Cell>> networkMap;
    private  List<Feature> allFeatures;
    private Map<String, Feature<Object>> featureMap = new HashMap<>();



    public void UserInterface(){
        UI.addButton("Login" , this::doLogin);
        UI.addButton("Latest Detections ", this::detections);
        UI.addButton("BlackList Port", this::blackListPort);
        UI.addButton("Print Log File", this::printMap);

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
            if(main.getCount()>MAXIMUM_FAILURES){
                UI.println("System shut down");
            }

        }else{
            UI.println("Please Sign in as Administrator");
        }
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

    }

    public void NetworkDataLoader(){
        main = new NetworkData(filePath); //initialise the NetworkData object
        main.loaders(); //load the xlsx file data and put into networkMap
        allFeatures = main.featureBuild(); //extract featureObjects from built networkMap
        networkMap = main.getLogFileMap(); //Get copy of networkMap for IntrusionDetector class
    }

    public void constructFeatureMap(){


    }


    public static void main(String[] args){
        IntrusionDetector id = new IntrusionDetector();
        id.loadPassword();
        id.NetworkDataLoader();
        id.constructFeatureMap();
        id.UserInterface();
    }
}
