package src.trainer;

import src.RequestFeatures.*;
import src.RequestFeatures.Date;

import java.util.*;
import src.trainer.Request;
import java.text.SimpleDateFormat;


/*THIS CLASS ATTEMPT TO TAKE IN ALL REQUESTS AS INPUTS IN A NEURAL NETWORK. THE REQUESTS EACH HAVE THEIR WEIGHT ABOUT
* HOW ANOMALOUS THE REQUEST IS. IT USES A SIGMOID ACTIVATION FUNCTION BETWEEN ONE AND ZERO*/

public class Node<T>{
    private T request;
    private double weight;
    private double output;
    private double[] features;

    public Node(T request){
        this.request = request;
        this.weight = Math.random(); //intialise weight to random double
        this.output = 0;
        this.features = extractFeatures( (Request) request);
    }

    public T getRequest(){
        return request;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public double getWeight(){
        return weight;
    }

    //calculate the output of the input by using sigmoid activation function

    public double calculateOutput(double input){
        double weightedSum = 0;
        for(int i = 0; i < features.length; i++){
            weightedSum+=features[i]*input;
        }
        this.output = sigmoidActivation(weightedSum);
        return output;
    }

    public double[] extractFeatures(Request request){

        double[] features = new double[7]; //working with 7 objects from request class
        features[0] = convertIpToNumeric(request.getIpAddress().getIp());
        features[1] = convertDateToNumeric(request.getDate());
        features[2] = convertTimeToNumeric(request.getTime());
        features[3] = convertUsernameToNumeric(request.getUsername());
        features[4] = convertUserTypeToNumeric(request.getUserType());
        features[5] = convertAccessToNumeric(request.getAcceptedFailed());
        features[6] = request.getPort().getPortNumber();

        return features;
    }

    public double sigmoidActivation(double x){
        return 1 / (1 + Math.exp(-x)); //sigmoid outputs a value in between 0 and 1;
    }

    public double convertIpToNumeric(String ip){
        String[] parts = ip.split("\\.");
        double numericIp = 0;
        for(int i = 0; i < parts.length; i++){
          numericIp+=Integer.parseInt(parts[i]) * Math.pow(256, 3 - i);
        }
        return numericIp;
    }

    public double convertDateToNumeric(Date dateObj){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateWithYear = dateObj.getDate() + "-" + Calendar.getInstance().get(Calendar.YEAR);
            java.util.Date parsedDate = formatter.parse(dateWithYear);
            return parsedDate.getTime();

        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public double convertTimeToNumeric(Time time){
        try{
            String[] parts = time.getTime().split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            return hours * 3600 + minutes * 60 + seconds; //converted into seconds

        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public double convertUsernameToNumeric(Username username){
        return username.hashCode();
    }

    public double convertUserTypeToNumeric(UserType userType){
       return userType.validUser ? 1 : 0;
    }

    public double convertAccessToNumeric(AcceptedFailed accessStatus){
        return accessStatus.acceptedFailed? 1 : 0;
    }

    public double getOutput(){
        return output;
    }



}
