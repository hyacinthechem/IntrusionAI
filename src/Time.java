package src;
public class Time{
    private String time;
    public static final String rowHeader = "TIME";


    public Time(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }

    public String getRowHeader(){
        return rowHeader;
    }

    public String toString(){
        return time;
    }
}
