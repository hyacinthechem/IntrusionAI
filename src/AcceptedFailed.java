<<<<<<< HEAD
package src;

=======
>>>>>>> 21a2dde32d698303c0263578cb2b402237219f29
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
