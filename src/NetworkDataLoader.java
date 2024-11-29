package src;

import java.util.*;
import ecs100.*;
import java.io.*;

/*
 *** Import .jar Apache POI library to utilize Excel file reading formats
 */



import java.io.File;
import java.io.FileInputStream;


public class NetworkDataLoader {
    private final String filename;
    private String extra;
    private final String filePivotTable;
    public boolean succesfullyLoaded = false;
    private String featureType;
    private String[] featureNames = {"DATE", "TIME", "ACCEPTED-FAILED", "USER-TYPE","USERNAME","IP-ADDRESS","PORT"};
    private List<String> featureList = Arrays.asList(featureNames);


    public NetworkDataLoader(String filename, String filePivotTable){
        this.filename = filename;
        this.filePivotTable = filePivotTable;
    }

    public void loaders() {


    }

    public void setFeatureType(String featureType){
        if(featureList.contains(featureType)){
            this.featureType = featureType;
        }else{
            UI.println("Feature type not recognized: " + featureType);
        }
    }

    public void loadFeatureType(String featureType) {

    }



    public boolean numberOfFailures(int failures){
        if(failures>IntrusionDetector.MAXIMUM_FAILURES){
            return true;
        }else{
            return false;
        }
    }


    public String getFileName(){
        return filename;
    }

    public String getFilePivotTable(){
        return filePivotTable;
    }

    public boolean succesfullyLoaded(){
        return succesfullyLoaded;
    }

}
