import java.util.*;
import ecs100.*;

public class NetworkDataLoader {
    private String filename = "sshd.log";
    private String filePivotTable = "sshd.ods";
    public boolean succesfullyLoaded = false;


    public NetworkDataLoader(String filename, String filePivotTable){
        this.filename = filename;
        this.filePivotTable = filePivotTable;
    }

    public void loaders(){


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
