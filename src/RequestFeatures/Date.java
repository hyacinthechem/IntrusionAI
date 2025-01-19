package src.RequestFeatures;

public class Date {
    private final String date;
    public static final String rowHeader = "DATE";

    public Date(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public String getRowHeader(){ return rowHeader;}

    public String toString(){
        return date;
    }

}
