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
    private List<Feature<?>> featureList;

    private Map<String, List<Cell>> sshdMap = new HashMap<>();

    //Feature Instantiations

    private Feature<AcceptedFailed> af;
    private Feature<Date> date;
    private Feature<IPAddress> ipAddress;
    private Feature<UserType> userType;
    private Feature<Username> username;
    private Feature<Port> port;
    private Feature<Time> time;


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
public List<Feature<?>> featureBuild() {
    featureList = new ArrayList<>(); // Instantiate the featureList
    af = new Feature<AcceptedFailed>();
    date = new Feature<Date>();
    ipAddress = new Feature<IPAddress>();
    port = new Feature<Port>();
    time = new Feature<Time>();
    username = new Feature<Username>();
    userType = new Feature<UserType>();


    for (String featureName : sshdMap.keySet()) { // Loop through the sshdMap keyset
        for (Cell cell : sshdMap.get(featureName)) { // Loop through cells in each column
            switch (featureName) {
                case "ACCEPTED-FAILED":
                    if (cell.getCellType() == CellType.STRING && "Accepted".equals(cell.getStringCellValue())) {
                        AcceptedFailed afd = new AcceptedFailed(true);
                        af.addFeature(afd);
                        featureList.add(af);
                    }
                    break;

                case "DATE":
                    if (cell.getCellType() == CellType.STRING) {
                        Date d = new Date(cell.getStringCellValue());
                        date.setColumnHeader(d.getRowHeader());
                        featureList.add(date);
                    }
                    break;

                case "IP-ADDRESS":
                    if (cell.getCellType() == CellType.STRING) { //check celltype to avoid exception thrown
                        IPAddress ip = new IPAddress(cell.getStringCellValue());
                        ipAddress.addFeature(ip);
                        featureList.add(ipAddress);
                    }
                    break;

                case "PORT":
                    if (cell.getCellType() == CellType.NUMERIC) { //check celltype to avoid exception thrown
                        Port p = new Port(cell.getNumericCellValue());
                        port.addFeature(p);
                        featureList.add(port);
                    }
                    break;

                case "TIME":
                    if (cell.getCellType() == CellType.STRING) {
                        Time t = new Time(cell.getStringCellValue());
                        time.addFeature(t);
                        featureList.add(time);
                    }
                    break;

                case "USERNAME":
                    if (cell.getCellType() == CellType.STRING) {
                        Username u = new Username(cell.getStringCellValue());
                        username.addFeature(u);
                        featureList.add(username);
                    }
                    break;

                case "USER-TYPE":
                    if (cell.getCellType() == CellType.STRING) {
                        UserType ut = new UserType("valid_user".equals(cell.getStringCellValue()));
                        userType.addFeature(ut);
                        featureList.add(userType);
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
