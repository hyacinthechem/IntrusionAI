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
    private int count;
    public boolean succesfullyLoaded = false;
    private String featureType;
    private String[] featureNames = {"DATE", "TIME", "ACCEPTED-FAILED", "USER-TYPE","USERNAME","IP-ADDRESS","PORT"};
    private List<String> featureList = Arrays.asList(featureNames);

    private Map<Row, List<Cell>> sshdMap = new HashMap<>();


    public NetworkDataLoader(String filename){
        this.filename = filename;
    }

    public void loaders() {
      try(FileInputStream fis = new FileInputStream(new File(filename));
        Workbook workbook = new XSSFWorkbook(fis)){


          Sheet sheet = workbook.getSheetAt(0); //returns pivot table of sshd
          for(Row row : sheet){
              List<Cell> cellList = new ArrayList<>();
              for(Cell cell : row) {
                  cellList.add(cell);
              }
              sshdMap.put(row, cellList);
          }

    }catch(IOException e){
        e.printStackTrace();
    }
}

    public void setFeatureType(String featureType){
        if(featureList.contains(featureType)){
            this.featureType = featureType;
        }else{
            UI.println("Feature type not recognized: " + featureType);
        }
    }

    public void loadFeatureType(String feature) {
        List<Cell> cellList = sshdMap.get(feature);
        for(Cell cell : cellList){
            if(cell.getStringCellValue().equals("Failed")){
                count++;
            }

        }


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

    public int getCount(){
        return count;
    }



    public boolean succesfullyLoaded(){
        return succesfullyLoaded;
    }

}
