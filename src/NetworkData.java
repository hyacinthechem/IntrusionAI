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
    private Map<Username, List<Request>> requestMap = new HashMap<>();

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

/*Initialise the method that creates the request objects from usernames. build a request map that has the username as key pairs and the List of
* requests from each user as a value pair. therefore Map<Username, List<Request>> */

public void loadAllRequests(){
    List<Request> requestList = new ArrayList<>();
    Date date = null;
    AcceptedFailed acceptedFailed = null;
    IPAddress ipAddress = null;
    Port port = null;
    Time time = null;
    UserType userType = null;
    Username username = null;

    try(FileInputStream fis = new FileInputStream(new File(filename));){
      Workbook workbook = new XSSFWorkbook(fis);
      Sheet sheet = workbook.getSheetAt(0);

      for(Row row : sheet){
          for(Cell cell : row){
            if (cell.getCellType() == CellType.STRING){
                String cellValue = cell.getStringCellValue();
                if(cellValue.startsWith("Apr") || cellValue.startsWith("March")){
                     date = new Date(cellValue);
                }

                if(cellValue.equals("Accepted")){
                     acceptedFailed = new AcceptedFailed(true);
                }else if(cellValue.equals("Failed")){
                     acceptedFailed = new AcceptedFailed(false);
                }

                if(cellValue.equals("valid_user")){
                     userType = new UserType(true);
                }else if(cellValue.equals("invalid_user")){
                     userType = new UserType(false);
                }

                int dotCount = 0;
                int colonCount = 0;
                char[] array = cellValue.toCharArray();
                for(int i = 0; i < array.length; i++){
                    if(array[i] == (':')){
                        colonCount++;
                    }

                    if(array[i] == ('.')){
                        dotCount++;
                    }
                }

                if(colonCount>2){
                     time = new Time(cellValue);
                }
                if(dotCount>2){
                     ipAddress = new IPAddress(cellValue);
                }

                List<String> ignoreColumn = ignoreColumnHeader();

                if(!(cellValue.startsWith("ssh2")) && !ignoreColumn.contains(cellValue)){
                    username = new Username(cellValue);
                }


            }

            if(cell.getCellType() == CellType.NUMERIC){
                double value = cell.getNumericCellValue();
                 port = new Port(value);
            }
          }
          Request request = new Request(ipAddress,date,time,username,userType,acceptedFailed,port);
          requestList.add(request);
          requestMap.put(username, requestList);
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
                    if (cell.getCellType() == CellType.STRING) {
                        AcceptedFailed afd = new AcceptedFailed(true);
                        af.setColumnHeader(afd.getRowHeader());
                        af.addFeature(afd);
                        featureList.add(af);
                    }
                    break;

                case "DATE":
                    if (cell.getCellType() == CellType.STRING) {
                        Date d = new Date(cell.getStringCellValue());
                        date.addFeature(d);
                        date.setColumnHeader(d.getRowHeader());
                        featureList.add(date);
                    }
                    break;

                case "IP-ADDRESS":
                    if (cell.getCellType() == CellType.STRING) { //check celltype to avoid exception thrown
                        IPAddress ip = new IPAddress(cell.getStringCellValue());
                        ipAddress.addFeature(ip);
                        ipAddress.setColumnHeader(ip.getRowHeader());
                        featureList.add(ipAddress);
                    }
                    break;

                case "PORT":
                    if (cell.getCellType() == CellType.NUMERIC) { //check celltype to avoid exception thrown
                        Port p = new Port(cell.getNumericCellValue());
                        port.setColumnHeader(p.getRowHeader());
                        port.addFeature(p);
                        featureList.add(port);
                    }
                    break;

                case "TIME":
                    if (cell.getCellType() == CellType.STRING) {
                        Time t = new Time(cell.getStringCellValue());
                        time.setColumnHeader(t.getRowHeader());
                        time.addFeature(t);
                        featureList.add(time);
                    }
                    break;

                case "USERNAME":
                    if (cell.getCellType() == CellType.STRING) {
                        Username u = new Username(cell.getStringCellValue());
                        username.setColumnHeader(u.getRowHeader());
                        username.addFeature(u);
                        featureList.add(username);
                    }
                    break;

                case "USER-TYPE":
                    if (cell.getCellType() == CellType.STRING) {
                        UserType ut = new UserType("valid_user".equals(cell.getStringCellValue()));
                        userType.setColumnHeader(ut.getRowHeader());
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

    public List<String> ignoreColumnHeader(){
         List<String> ignoreColumnHeader = new ArrayList<>();
         ignoreColumnHeader.add("app-1");
         ignoreColumnHeader.add("password");
         ignoreColumnHeader.add("for");
         ignoreColumnHeader.add("from");
         ignoreColumnHeader.add("port");
         return ignoreColumnHeader;
    }



    public boolean succesfullyLoaded(){
        return succesfullyLoaded;
    }

}
