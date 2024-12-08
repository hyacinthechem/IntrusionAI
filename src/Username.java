package src;
public class Username {

    private final String username;
    public static final String rowHeader = "USERNAME";


    public Username(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public String getRowHeader(){
        return rowHeader;
    }

    public String toString(){
        return username;
    }
}
