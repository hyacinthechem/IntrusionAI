package src;

import java.util.*;
import java.io.*;


/*
 *** Import .jar Apache POI library to utilize Excel file reading formats
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlAnySimpleType;

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
    private final Set<String> ignoredColumns = Set.of("app-1","password","for","from","port");


    //Feature Instantiations



    private Feature<AcceptedFailed> featureAf;
    private Feature<Date> featureDate;
    private Feature<IPAddress> featureipAddress;
    private Feature<UserType> featureUserType;
    private Feature<Username> featureUsername;
    private Feature<Port> featurePort;
    private Feature<Time> featureTime;




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

try(FileInputStream fis = new FileInputStream(new File(filename))){
       Workbook workbook = new XSSFWorkbook(fis);
       Sheet sheet = workbook.getSheetAt(0);
       for(Row row : sheet){
           Request request = parseRow(row); //each row is a request so separate with a method that returns request
           if(request!=null){
               requestMap.computeIfAbsent(request.getUsername(), k->new ArrayList<>()).add(request);
           }
       }

}catch(IOException e){
    e.printStackTrace();
   }
}

private Request parseRow(Row row){
  try{
      Date date = null;
      AcceptedFailed accessStatus = null;
      IPAddress ipAddress = null;
      Port port = null;
      Time time = null;
      UserType userType = null;
      Username username = null;
      // Now we've initialised features to null, we got through each column and utilise switch statement between column index to create the right feature e.g. "Date" "Time" "UserType"
      for(Cell cell : row){
          String header = row.getSheet().getRow(0).getCell(cell.getColumnIndex()).getStringCellValue(); //returns the string of the column it is on
          if(ignoredColumns.contains(header)){
              continue; //continue means skip
          }
          switch(header){ //switches through each header of a column and then creates an object for each feature
              case "DATE"-> date = new Date(cell.getStringCellValue());
              case "TIME" -> time = new Time(cell.getStringCellValue());
              case "ACCEPTED-FAILED" -> accessStatus = new AcceptedFailed("ACCEPTED".equalsIgnoreCase(cell.getStringCellValue()));
              case "USER-TYPE" -> userType = new UserType("valid_user".equalsIgnoreCase(cell.getStringCellValue()));
              case "USERNAME" -> username = new Username(cell.getStringCellValue());
              case "IP-ADDRESS" -> ipAddress = new IPAddress(cell.getStringCellValue());
              case "PORT" -> port = new Port(cell.getNumericCellValue());
              default -> System.out.println("Unknown Column" + header);
          }
      }
      if(username!=null) {
          return new Request(ipAddress, date, time, username, userType, accessStatus, port);
      }
  }catch(Exception e){
      System.out.println("Error parsing row" + e.getMessage());
  }
    return null;
}

public void loadAllRequests1(){
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

                if(colonCount==2){
                     time = new Time(cellValue);
                }
                if(dotCount>2){
                     ipAddress = new IPAddress(cellValue);
                }

                if(!(cellValue.startsWith("ssh2")) && !ignoredColumns.contains(cellValue) && !(dotCount>2)){  /*  */
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
    featureAf = new Feature<AcceptedFailed>();
    featureDate = new Feature<Date>();
    featureipAddress = new Feature<IPAddress>();
    featurePort = new Feature<Port>();
    featureTime = new Feature<Time>();
    featureUsername = new Feature<Username>();
    featureUserType = new Feature<UserType>();


    for (String featureName : sshdMap.keySet()) { // Loop through the sshdMap keyset
        for (Cell cell : sshdMap.get(featureName)) { // Loop through cells in each column
            switch (featureName) {
                case "ACCEPTED-FAILED":
                    if (cell.getCellType() == CellType.STRING) {
                        AcceptedFailed afd = new AcceptedFailed(true);
                        featureAf.setColumnHeader(afd.getRowHeader());
                        featureAf.addFeature(afd);
                        featureList.add(featureAf);
                    }
                    break;

                case "DATE":
                    if (cell.getCellType() == CellType.STRING) {
                        Date d = new Date(cell.getStringCellValue());
                        featureDate.addFeature(d);
                        featureDate.setColumnHeader(d.getRowHeader());
                        featureList.add(featureDate);
                    }
                    break;

                case "IP-ADDRESS":
                    if (cell.getCellType() == CellType.STRING) { //check celltype to avoid exception thrown
                        IPAddress ip = new IPAddress(cell.getStringCellValue());
                        featureipAddress.addFeature(ip);
                        featureipAddress.setColumnHeader(ip.getRowHeader());
                        featureList.add(featureipAddress);
                    }
                    break;

                case "PORT":
                    if (cell.getCellType() == CellType.NUMERIC) { //check celltype to avoid exception thrown
                        Port p = new Port(cell.getNumericCellValue());
                        featurePort.setColumnHeader(p.getRowHeader());
                        featurePort.addFeature(p);
                        featureList.add(featurePort);
                    }
                    break;

                case "TIME":
                    if (cell.getCellType() == CellType.STRING) {
                        Time t = new Time(cell.getStringCellValue());
                        featureTime.setColumnHeader(t.getRowHeader());
                        featureTime.addFeature(t);
                        featureList.add(featureTime);
                    }
                    break;

                case "USERNAME":
                    if (cell.getCellType() == CellType.STRING) {
                        Username u = new Username(cell.getStringCellValue());
                        featureUsername.setColumnHeader(u.getRowHeader());
                        featureUsername.addFeature(u);
                        featureList.add(featureUsername);
                    }
                    break;

                case "USER-TYPE":
                    if (cell.getCellType() == CellType.STRING) {
                        UserType ut = new UserType("valid_user".equals(cell.getStringCellValue()));
                        featureUserType.setColumnHeader(ut.getRowHeader());
                        featureUserType.addFeature(ut);
                        featureList.add(featureUserType);
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

    public Map<Username, List<Request>> getRequestMap(){
      return Collections.unmodifiableMap(requestMap);
    }

}
