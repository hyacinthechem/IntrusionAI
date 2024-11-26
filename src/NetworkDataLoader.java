package src;

import java.util.*;
import ecs100.*;
import java.io.*;

/*
 *** Import .jar Apache POI library to utilize Excel file reading formats
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
<<<<<<< HEAD
        this.filename = filename;
        this.filePivotTable = filePivotTable;
    }

    public void loaders() {

        String filePath = filename;
        String odsPath = filePivotTable;
        try{
        File odsFile = new File(odsPath); //create new file object with filePivotTable string path
        SpreadSheet document = SpreadSheet.createFromFile(odsFile); //create spreadsheet object utilising odsFile object
        Sheet sheet = document.getSheet(0); //gets the first sheet in ssh.ods which is the ssh log file pivot table

        //Read each of the rows and columns

        for(int row=0; row<sheet.getRowCount(); row++ ){ //loop through each of the rows
            for(int col = 0; col<sheet.getColumnCount(); col++){
                Object cellValue = sheet.getCellAt(col,row).getValue();

                UI.println(cellValue + "\t");
            }
            UI.println();
          }

        succesfullyLoaded = true;

        }catch(IOException e){
            UI.println("File Failure" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
=======

>>>>>>> 441c3d4 (Maven Libraries)
    }

    public void setFeatureType(String featureType){
        if(featureList.contains(featureType)){
            this.featureType = featureType;
        }else{
            UI.println("Feature type not recognized: " + featureType);
        }
    }

    public void loadFeatureType(String featureType) {
<<<<<<< HEAD
        int count = 0;
        setFeatureType(featureType); // sets the featureType within the same class
        try {
            File file = new File(filePivotTable);
            UI.println("Loading features from " + filePivotTable);
            SpreadSheet document = SpreadSheet.createFromFile(file);
            UI.println("Loaded Succesfully");
            Sheet sheet = document.getSheet(0); // individual sheet from ods file
            UI.println("Loading Specific Sheet");

            for (int row = 0; row < sheet.getRowCount(); row++) {
                Object rowValue = sheet.getCellAt(0, row).getValue(); // Get row name
                UI.println("Loaded Sheet");
                UI.println(rowValue + "\t");
                if (rowValue != null && rowValue instanceof String) { // Ensure it's a string
                    String rowName = (String) rowValue;
                    if (rowName.equals(featureType)) {
                        for (int col = 0; col < sheet.getColumnCount(); col++) {
                            Object cellValue = sheet.getCellAt(col, row).getValue();
                            UI.println(cellValue + "\t");
                            if (cellValue != null && cellValue instanceof String) {
                                String cell = (String) cellValue;
                                extra = "yes";
                                if ("Failed".equals(cell)) {
                                    count++;
                                    if (numberOfFailures(count)) {
                                        UI.println("Exceeded maximum failures!");
                                        break;
                                    }
                                }
                            }
                        }
                        break; // Exit the loop after processing the row
                    }
                }
            }
            UI.println("Loaded " + count + " features" + (extra != null ? " - " + extra : ""));
        } catch (IOException e) {
            UI.println("File Failure: " + e.getMessage());
        }
=======

>>>>>>> 441c3d4 (Maven Libraries)
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
