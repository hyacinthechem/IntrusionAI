package src.trainer;

public class NeuralNetwork {
    private int numberOfNeurons;
    private int numberOfLayers;
    private double[][] weights;
    private double[][] biases;

    public NeuralNetwork(int numberOfNeurons, double[][] weights, double[][] biases) {
        this.numberOfNeurons = numberOfNeurons;
        this.weights = weights;
        this.biases = biases;
    }

    public void setNumberOfLayers(int numberOfLayers){
        this.numberOfLayers = numberOfLayers;
    }
}
