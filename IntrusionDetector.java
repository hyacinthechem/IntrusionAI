import java.lang.annotation.ElementType;
import java.util.*;
import java.io.*;
import ecs100.*;
import javax.swing.*;


public class IntrusionDetector extends JFrame {

    private boolean administrator;
    private String adminPassword;
    private String filePath = "data/sshd.log";
    private String odsFilePath = "data/sshd.ods";

    Map<String, Feature<Object>> featureMap = new HashMap<>();



    public void UserInterface(){
        UI.addButton("Login" , this::doLogin);
        UI.addButton("Latest Detections ", this::detections);

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
            NetworkDataLoader ndl = new NetworkDataLoader(filePath, odsFilePath);
            ndl.loaders();
        }else{
            UI.println("Please Sign in as Administrator");
        }
    }


    public static void main(String[] args){
        IntrusionDetector id = new IntrusionDetector();
        id.loadPassword();
        id.UserInterface();
    }
}
