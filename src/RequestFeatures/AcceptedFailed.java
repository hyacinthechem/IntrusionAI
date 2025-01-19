package src.RequestFeatures;
public class AcceptedFailed{

    private boolean acceptedFailed;
    public static final String rowHeader = "ACCEPTED-FAILED";


    public AcceptedFailed(boolean acceptedFailed) {
        this.acceptedFailed = acceptedFailed;
    }

    public boolean isAccepted(){
        return acceptedFailed;
    }

    public boolean isFailed(){
        return !acceptedFailed;
    }

    public String getRowHeader(){ return rowHeader;}

    public String toString(){
        return acceptedFailed?"Accepted":"Failed";
    }
}
