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

    private Map<String, List<Cell>> sshdMap = new HashMap<>();


    public NetworkDataLoader(String filename){
        this.filename = filename;
    }

    public void loaders() {
      try(FileInputStream fis = new FileInputStream(new File(filename));
        Workbook workbook = new XSSFWorkbook(fis)){
          Sheet sheet = workbook.getSheetAt(0);
          int maxRow = sheet.getLastRowNum(); //returns maximum amount of rows
          int maxCol = sheet.getRow(0).getLastCellNum(); //returns maximum amount of cols

          /* iterate through the columns first in regular for loop*/
          for(int i = 0; i < maxCol; i++){
              List<Cell> cellList = new ArrayList<>();
              String columnName = sheet.getRow(0).getCell(i).getStringCellValue();
              for(int j = 0; j < maxRow; j++){
                  Row row = sheet.getRow(j); //get the current row we are on from column
                  Cell currentCell = row.getCell(i); //retrieve the cell we are on from i
                  if(currentCell!=null){
                      cellList.add(currentCell);
                  }
              }
              sshdMap.put(columnName,cellList);

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

    public Map<String, List<Cell>> getLogFileMap(){
        return Collections.unmodifiableMap(sshdMap);
    }



    public boolean succesfullyLoaded(){
        return succesfullyLoaded;
    }

}
