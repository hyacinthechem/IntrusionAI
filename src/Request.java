package src;
import java.util.*;

/*This class is utilised to create a request object which consists a Row of Data being the request made by a
* computer from a specific address ( IP address ) with port, date, time, username, usertype etc.
* In Simple terms it is a request made from a local machine to the specified network
* */

public class Request {
    private IPAddress ipAddress;
    private Date date;
    private Time time;
    private Username username;
    private UserType userType;
    private AcceptedFailed accessStatus;
    private Port port;

    /*Constructor calls*/

    public Request(IPAddress ipAddress, Date date, Time time, Username username, UserType userType, AcceptedFailed accessStatus, Port port){
        this.ipAddress = ipAddress;
        this.date = date;
        this.time = time;
        this.username = username;
        this.userType = userType;
        this.accessStatus = accessStatus;
        this.port = port;
    }

    public Request(IPAddress ip, Date date, Time time){
        this.ipAddress = ip;
        this.date = date;
        this.time = time;
    }

    public Request(Username username, UserType userType, AcceptedFailed accessStatus){
        this.username = username;
        this.userType = userType;
        this.accessStatus = accessStatus;
    }

    /* Getter Methods */

    public IPAddress getIpAddress(){
        return ipAddress;
    }

    public Date getDate(){
        return date;
    }

    public Time getTime(){
        return time;
    }

    public Username getUsername(){
        return username;
    }

    public UserType getUserType(){
        return userType;
    }

    public AcceptedFailed getAcceptedFailed(){
        return accessStatus;
    }

    public Port getPort(){
        return port;
    }

    @Override

    /*Specific toString method that shows only who made the request and at what time*/

    public String toString(){
        return username.toString() + time.toString();
    }
}
