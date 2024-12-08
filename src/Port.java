package src;
public class Port {
    public final double port;
    public static final String rowHeader = "PORT";


    public Port(double port){
        this.port = port;
    }

    public double getPortNumber(){
        return port;
    }

    public String getRowHeader(){
        return rowHeader;
    }

    public String toString(){
        return Double.toString(port);
    }
}
