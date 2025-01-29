package src.trainer;

import src.main.NetworkData;

import java.util.ArrayList;
import java.util.List;

public class NetworkProcessor {
    /*The implemeneted neural network that utilises the nodes and sets up batch numbers and epochs*/

    //epochs, batch, learning rate, NeuralNetwork, LossFunction

    private double numberOfEpochs;
    private int batchSize;
    private final NetworkData requestDataSet;
    private final NeuralNetwork model;
    private final LossFunction lossFunction;
    private final LearningRate learningRate;

    public NetworkProcessor(
       NeuralNetwork model,
       NetworkData requestDataSet,
       double numberOfEpochs,
       int batchSize,
       LossFunction lossFunction,
       LearningRate learningRate
    ){
        this.model = model;
        this.requestDataSet = requestDataSet;
        this.numberOfEpochs = numberOfEpochs;
        this.batchSize = batchSize;
        this.lossFunction = lossFunction;
        this.learningRate = learningRate;
    }


    public void trainModel(){
        List<Request> requests = requestDataSet.getAllRequests();
        List<Node<Request>> inputsForNeuralNetwork = new ArrayList<>(); //get All the inputs from file
        for(Request request : requests){
            Node<Request> node = new Node<Request>(request);
            inputsForNeuralNetwork.add(node);
        }
      for(int i = 0; i < batchSize; i++){ //loop through and choose by the amount that the batch size is set to
          Node<Request> req = inputsForNeuralNetwork.get(i);
          assignInput(i); //assign the request to a neuron in the first layer

      }

    }

    public void assignInput(int neuronNumber){

    }

    public void runFinalModel(){

    }



    /*
    * GETTERS AND SETTERS
    *
    * */

    public NeuralNetwork getModel(){
        return model;
    }

    public NetworkData getRequestDataSet(){
        return requestDataSet;
    }

    public double getNumberOfEpochs(){
        return numberOfEpochs;
    }

    public int getBatchSize(){
        return batchSize;
    }

    public LossFunction getLossFunction(){
        return lossFunction;
    }

    public LearningRate getLearningRate(){
        return learningRate;
    }

    public void setNumberOfEpochs(double numberOfEpochs){
        this.numberOfEpochs = numberOfEpochs;
    }

    public void setBatchSize(int batchSize){
        this.batchSize = batchSize;
    }
}
