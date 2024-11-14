import java.util.*;
import ecs100.*;
import java.io.*;

/*
*** Import .jar ODF Toolkit library to utilise odf file reading formats
 */

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import java.io.File;
import java.io.FileInputStream;


public class NetworkDataLoader {
    private final String filename;
    private final String filePivotTable;
    public boolean succesfullyLoaded = false;


    public NetworkDataLoader(String filename, String filePivotTable){
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
