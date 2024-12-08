
package src;

public class IPAddress {

    private final String ip;
    public static final String rowHeader = "IP-ADDRESS";


    public IPAddress(String ip){
        this.ip = ip;
    }

    public String getIp(){
        return ip;
    }

    public String getRowHeader(){
        return rowHeader;
    }

    public String toString(){
        return ip;
    }
}
