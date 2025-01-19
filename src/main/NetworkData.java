package src.main;

import java.util.*;
import java.io.*;
import src.RequestFeatures.*;
import src.RequestFeatures.Date;
import src.main.Request;


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
    private Map<String, List<Cell>> sshdMap = new HashMap<>();
    private Map<Username, List<Request>> requestMap = new HashMap<>();
    private final List<Request> allRequests = new ArrayList<>();
    private final Set<String> ignoredColumns = Set.of("app-1","password","for","from","port","IGNORE");



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
           allRequests.add(request);
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
              case "USERNAME" -> {
                  if(CellType.STRING.equals(cell.getCellType())){
                      username = new Username(cell.getStringCellValue());
                  }else if(CellType.NUMERIC.equals(cell.getCellType())){
                      username = new Username(String.valueOf((long)(cell.getNumericCellValue())));
                  }
              }
              case "IP-ADDRESS" -> ipAddress = new IPAddress(cell.getStringCellValue());
              case "PORT" -> port = new Port(cell.getNumericCellValue());
              default -> System.out.println("Unknown Column" + header);
          }
      }
      if(username!=null) {
          return new Request(ipAddress, date, time, username, userType, accessStatus, port);
      }
  }catch(Exception e){
      e.printStackTrace();
      StackTraceElement[] stackTrace = e.getStackTrace();
      System.out.println("Error Occured on Line" + stackTrace[0].getLineNumber());
      System.out.println("Error parsing row" + e.getMessage());
  }
    return null;
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

    public List<Request> getAllRequests(){return Collections.unmodifiableList(allRequests);}

}
