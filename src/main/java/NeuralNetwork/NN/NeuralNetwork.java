package NeuralNetwork.NN;

import NeuralNetwork.ConvNet.FeatureLayer;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import NeuralNetwork.math;

/**
 * The NeuralNetwork is a classic multilayer perceptron
 */
public class NeuralNetwork implements Serializable {
    ArrayList<MiniBatch> miniBatches = new ArrayList<>();
    private ArrayList<double[][]> W = new ArrayList<>();
    private ArrayList<double[][]> B = new ArrayList<>();
    private ArrayList<double[][]> VdW = new ArrayList<>();
    private ArrayList<double[][]> VdB = new ArrayList<>();
    private double[][] Y = null;
    double beta;

    public NeuralNetwork(int[] layerSizes, FeatureLayer featureLayer, int miniBatchSize, int seed, double[][] Y, double... beta ){
        double[][] tempLayer = featureLayer.flatten(0,1);
        this.Y = Y;
        if(beta.length != 0) {
            assert 0<beta[0] && beta[0] <=1;
            this.beta = beta[0];
            VdW.add(math.multiply(2.0/(tempLayer.length), math.zero(layerSizes[0],tempLayer.length)));
            VdB.add(math.multiply(2.0/(tempLayer.length), math.zero(layerSizes[0],1)));
        }
        //create W and B
        W.add(math.multiply(2.0/(tempLayer.length), math.random(layerSizes[0],tempLayer.length, seed)));
        B.add(math.multiply(2.0/(tempLayer.length), math.random(layerSizes[0],1,seed)));
        for (int l = 0; l < layerSizes.length-1; l++) {
            int dim_cur = layerSizes[l];
            int dim_next = layerSizes[l + 1];
            W.add(math.multiply(2.0/(dim_cur), math.random(dim_next, dim_cur,seed+1)));
            B.add(math.multiply(2.0/(dim_cur), math.random(dim_next, 1,seed+1)));
            if(beta.length != 0){
                VdW.add(math.multiply(2.0/(dim_cur), math.zero(dim_next, dim_cur)));
                VdB.add(math.multiply(2.0/(dim_cur), math.zero(dim_next, 1)));
            }
        }

        //split featureLayer into mini batches base on the miniBatchSize
        int fullyMiniBatches = featureLayer.size() / miniBatchSize;
        int remaining = featureLayer.size() - (fullyMiniBatches * miniBatchSize);
        int start = 0;
        int end = start + miniBatchSize;
        for(int i=0; i<fullyMiniBatches; i++){
            if (this.beta == 0) {
                miniBatches.add(new MiniBatch(W, B, featureLayer.flatten(start, end), math.choseColumn(Y, start, end)));
            }else{
                miniBatches.add(new MiniBatch(W,B,VdW,VdB, featureLayer.flatten(start, end),
                        math.choseColumn(Y, start, end),this.beta));
            }

            start += miniBatchSize;
            end += miniBatchSize;
        }
        if (remaining != 0) {
            end = start+remaining;
            if (this.beta == 0) {
                miniBatches.add(new MiniBatch(W, B, featureLayer.flatten(start, end), math.choseColumn(Y, start, end)));
            }else{
                miniBatches.add(new MiniBatch(W,B,VdW,VdB, featureLayer.flatten(start, end),
                        math.choseColumn(Y, start, end),this.beta));
            }
        }
    }

    // for prediction
    public NeuralNetwork(ArrayList<double[][]> W,ArrayList<double[][]> B, FeatureLayer f, double[][] Y){
        this.W = W;
        this.B = B;
        this.Y = Y;
        this.miniBatches.add(new MiniBatch(W,B, f.flatten()));
    }

    // for processImage
    public NeuralNetwork(Parameters p, FeatureLayer f){
        this.W = p.getW();
        this.B = p.getB();
        this.miniBatches.add(new MiniBatch(W, B, f.flatten()));
    }

    /**
     * method is used to predict the accuracy of the test set
     * @return probability of each category for all images within the test set
     */
    public void predict(){
        MiniBatch batch = miniBatches.get(0);
        double[][] AL = batch.forward_prop();
        Helper.accuracy(AL,Y);
    }

    public double[] predict_Democratic(){
        MiniBatch batch = miniBatches.get(0);
        double[] result = batch.predict_Democratic();
        return result;
    }
    /**
     * The method trains the network using mini batch gradient descent
     * a mini batch can be an entire training set or smaller subsets, usually 2^x
     * @param learningRate
     * @param iterations
     */
    public void train(double learningRate, int iterations){
        for (int i = 0; i < iterations; i++) {
            System.out.println("The "+ i +"th epoch:");
            for(MiniBatch miniBatch:miniBatches){
                miniBatch.train(learningRate);
            }
            System.out.println();
        }
    }

    //getter methods.
    public ArrayList<double[][]> getW() {
        return W;
    }

    public ArrayList<double[][]> getB() {
        return B;
    }
}
