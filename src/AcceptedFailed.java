package src;
public class AcceptedFailed {
    private boolean acceptedFailed;

    public AcceptedFailed(boolean acceptedFailed) {
        this.acceptedFailed = acceptedFailed;
    }

    public boolean isAccepted(){
        return acceptedFailed;
    }

    public boolean isFailed(){
        return !acceptedFailed;
    }
}
