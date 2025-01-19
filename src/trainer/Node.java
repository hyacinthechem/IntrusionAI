package src.trainer;
import java.util.*;

/*THIS CLASS ATTEMPT TO TAKE IN ALL REQUESTS AS INPUTS IN A NEURAL NETWORK. THE REQUESTS EACH HAVE THEIR WEIGHT ABOUT
* HOW ANOMALOUS THE REQUEST IS. IT USES A SIGMOID ACTIVATION FUNCTION BETWEEN ONE AND ZERO*/

public class Node<Request>{
    private Request request;
    private double weight;

    public Node(Request request){
        this.request = request;
    }

    public Request getRequest(){
        return request;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }

    public double getWeight(){
        return weight;
    }
}
