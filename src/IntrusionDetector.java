
package src;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.util.*;
import java.io.*;
import java.util.List;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import ecs100.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.City;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class IntrusionDetector extends JFrame {

    public static final int MAXIMUM_FAILURES = 20;

    private File networkFile;
    private DatabaseReader reader;
    private boolean administrator;
    private boolean portRisk = false;
    private double infectedPorts;
    private String adminPassword;
    private String filePath = "data/sshd.xlsx";
    private NetworkData main;

    private List<Request> allRequests;
    private Map<String, List<Cell>> networkMap;
    private List<Double> portNumbers;
    private Map<Username, List<Request>> requestMap;


    public void UserInterface() {
        UI.addButton("Login", this::doLogin);
        UI.addButton("Latest Detections ", this::detections);
        UI.addButton("BlackList Port", this::blackListPort);
        UI.addButton("Print Log File", this::printMap);
        UI.addButton("Debug", this::debugger);

    }

    public IntrusionDetector() {
        setTitle("IntrusionAI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JTextField administratorPassword = new JTextField("Admin Password");
        add(administratorPassword);

        administratorPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPassword();
                System.out.println(adminPassword);
                String password = JOptionPane.showInputDialog("Enter Admin Password");
                if (password != null && password.equals(adminPassword)) {
                    administrator = true;
                    JOptionPane.showMessageDialog(null, "Password is Correct");
                } else {
                    administrator = false;
                    JOptionPane.showMessageDialog(null, "Password is incorrect");
                }
            }
        });
        JLabel infectedPortsLabel = new JLabel("Infected Ports");
        add(infectedPortsLabel);
        JButton detections = new JButton("Detections");
        add(detections);

        JButton openButton = new JButton("Open File");
        openButton.setBounds(0, 0, 50, 30);
        add(openButton);
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new Object for the file management
                JFileChooser fileChooser = new JFileChooser(); //empty constructor call
                fileChooser.setDialogTitle("Choose Network Log File");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) { //this is the value of
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    networkFile = selectedFile;
                    JOptionPane.showMessageDialog(null, "Selected Network Log File: " + selectedFile.getAbsolutePath());
                    detections();
                }
            }
        });
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
            if(portNumbers!=null) {
                portScanner(); //>: 57897 33041 39387
            }
            invalidUserRequests();
            ipAddressRouting();
        }else{
            UI.println("Please Sign in as Administrator");
        }
    }

    public void ipAddressRouting(){
        Map<String, Integer> ipLookup = new TreeMap<>();
        for(Request request : allRequests){
           IPAddress ip = request.getIpAddress();
           String ipBit = ip.toString();
           String location = ipConnect(ipBit);
           if(!ipLookup.containsKey(location)){
               ipLookup.put(location,1);
           }else{
               ipLookup.put(location,ipLookup.get(location)+1);
           }
       }
        List<Map.Entry<String,Integer>> lookupList = new ArrayList<>(ipLookup.entrySet());
        lookupList.sort(new Comparator<Map.Entry<String,Integer>>(){
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2){
              return o2.getValue().compareTo(o1.getValue());
            }
        });

       for(Map.Entry<String, Integer> entry : lookupList){
           String location = entry.getKey();
           int locationValue = entry.getValue();
           UI.println("Most Frequent locations: " + location + " Number Of Requests: " + locationValue);
       }
    }

    public String ipConnect(String ipBit){
        try{
            File dataFile  = new File("data/GeoLite2-City.mmdb");
            reader = new DatabaseReader.Builder(dataFile).build();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            InetAddress ip = InetAddress.getByName(ipBit); //get the net request for ipString
            CityResponse response = reader.city(ip); //read the netAddress
            String city = response.getCity().getName(); //extract string from CityResponse
            if(city==null){
                city = "Unavailable, ";
            }
            String country = response.getCountry().getName(); //get the country of response

            return city + ", " + country;

        }catch(IOException | GeoIp2Exception e){
            e.printStackTrace();
            return "Location not found";
        }
    }

    public void invalidUserRequests(){
        int invalidRequests = 0;
        for(Request req : allRequests){
            UserType ut = req.getUserType();
            if(ut.isInvalidUser()){
                invalidRequests++;
            }
        }
        UI.println("Invalid Requests: " + invalidRequests);
    }

    public void portScanner(){
        for(Request req : allRequests){
            Port port = req.getPort();
            if(portNumbers.contains(port.getPortNumber())){
                infectedPorts++;
            }
        }
        UI.println("Detected: " + infectedPorts + " Infected Ports");
    }


    public void acceptedFailed(){
        int count = 0;
        for(Request req : allRequests){
            if(req.getAcceptedFailed().isFailed()){
                count++;
            }
        }

        if(count > MAXIMUM_FAILURES) {
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
        main.loadAllRequests();
        networkMap = main.getLogFileMap(); //Get copy of networkMap for IntrusionDetector class
        requestMap = main.getRequestMap();
        allRequests = new ArrayList<>();
        allRequests.addAll(requestMap.values().stream().flatMap(List::stream).toList()); //flattens all requests from map and converts to fit into list
    }

    /* Feature Map must have rowHeaders "PORT", "DATE" "TIME" ETC as the Key,
    and then the list of the features being the columns as the Pair Value*

    So then you have size 14 and then the list of all the columns for that feature
     */



    public static void main(String[] args){

        SwingUtilities.invokeLater(() -> {
            IntrusionDetector detector = new IntrusionDetector();
            detector.NetworkDataLoader();
            detector.setVisible(true);
        });


        IntrusionDetector id = new IntrusionDetector();
        id.loadPassword();
        id.NetworkDataLoader();
        //id.UserInterface();
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