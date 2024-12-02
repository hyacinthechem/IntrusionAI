package src;

import java.util.*;
import java.io.*;

/*
 *** Import .jar Apache POI library to utilize Excel file reading formats
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;



public class NetworkData {
    private final String filename;
    private int count;
    public boolean succesfullyLoaded = false;
    private String[] featureNames = {"DATE", "TIME", "ACCEPTED-FAILED", "USER-TYPE","USERNAME","IP-ADDRESS","PORT"};
    private List<Feature> featureList;

    private Map<String, List<Cell>> sshdMap = new HashMap<>();


    public NetworkData(String filename){
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


/* Feature Build method enters the sshdMap and constructs objects based on its featureType for all features. */
public List<Feature> featureBuild() {
    featureList = new ArrayList<>(); // Instantiate the featureList

    for (String featureName : sshdMap.keySet()) { // Loop through the sshdMap keyset
        for (Cell cell : sshdMap.get(featureName)) { // Loop through cells in each column
            switch (featureName) {
                case "ACCEPTED-FAILED":
                    if (cell.getCellType() == CellType.STRING && "Accepted".equals(cell.getStringCellValue())) {
                        AcceptedFailed af = new AcceptedFailed(true);
                        featureList.add(new Feature(af));
                    }
                    break;

                case "DATE":
                    if (cell.getCellType() == CellType.STRING) {
                        Date d = new Date(cell.getStringCellValue());
                        featureList.add(new Feature(d));
                    }
                    break;

                case "IP-ADDRESS":
                    if (cell.getCellType() == CellType.STRING) { //check celltype to avoid exception thrown
                        IPAddress ip = new IPAddress(cell.getStringCellValue());
                        featureList.add(new Feature(ip));
                    }
                    break;

                case "PORT":
                    if (cell.getCellType() == CellType.NUMERIC) { //check celltype to avoid exception thrown
                        Port p = new Port(cell.getNumericCellValue());
                        featureList.add(new Feature(p));
                    }
                    break;

                case "TIME":
                    if (cell.getCellType() == CellType.STRING) {
                        Time t = new Time(cell.getStringCellValue());
                        featureList.add(new Feature(t));
                    }
                    break;

                case "USERNAME":
                    if (cell.getCellType() == CellType.STRING) {
                        Username u = new Username(cell.getStringCellValue());
                        featureList.add(new Feature(u));
                    }
                    break;

                case "USER-TYPE":
                    if (cell.getCellType() == CellType.STRING) {
                        UserType ut = new UserType("valid_user".equals(cell.getStringCellValue()));
                        featureList.add(new Feature(ut));
                    }
                    break;
            }
        }
    }

    return featureList;
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
