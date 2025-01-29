package src.trainer;

import src.main.NetworkData;
import src.main.IntrusionDetector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;
import java.util.Random;

public class NeuralNetwork {

    private int numberOfLayers;
    private IntrusionDetector intrusionDetector = new IntrusionDetector(); //check for main app vulnerability
    private List<Request> requests = intrusionDetector.allRequests; //check for any vulnerable use cases
    private int numberOfNeurons;
    private List<Node<Request>> nodes;

    public NeuralNetwork(int numberOfLayers, int numberOfNeurons){
        this.numberOfLayers = numberOfLayers;
        this.numberOfNeurons = numberOfNeurons;
        this.nodes = new ArrayList<>();

        //intialise the nodes
        for(int i = 0; i < numberOfNeurons; i++){
            Random rand = new Random();
            int index = rand.nextInt(1000); //check if bound causes issues
            Request req = requests.get(index);
            Node<Request> node = new Node<>(req);
            nodes.add(node);
        }
    }

    public void forwardPass(double[] inputs){
       for(int i = 0; i < nodes.size(); i++){
           Node<Request> node = nodes.get(i);
           node.calculateOutput(inputs[i]);
       }
    }

    public List<Node<Request>> getNodes(){
        return Collections.unmodifiableList(nodes);
    }

    //!!! THIS IS THE IMPORTANT METHOD THAT CALCULATES THE FINAL DECISION BOUNDARY BETWEEN 0 AND 1 TO CLASSIFY AN
    //!!! INSTANCE AS ANOMALOUS OR NON ANOMALOUS

    public double getNetworkOutput(){
        Node<Request> node = nodes.get(nodes.size()-1);
        return node.getOutput();
    }

    public void updateWeights(double[] weightUpdates){
        for(int i = 0; i < nodes.size(); i++){
            Node<Request> node = nodes.get(i);
            node.setWeight(node.getWeight() + weightUpdates[i]);
        }
    }
}
