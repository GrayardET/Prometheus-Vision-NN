package Experiment;

import NeuralNetwork.ConvNet.FeatureBlock;
import NeuralNetwork.ConvNet.FeatureLayer;
import NeuralNetwork.NN.MiniBatch;
import NeuralNetwork.NN.Helper;
import NeuralNetwork.math;
import Processing.Option;
import Processing.Preprocessing;

import java.util.ArrayList;
import java.util.Arrays;

import static NeuralNetwork.NN.LinePlotter.plotCost;

public class NNTest {
    public static void main(String args[]) throws Exception {

//        //test Helper.Linear_forward
//        double[][] w1 = {{1,3,2},{5,4,6}};
//        double[][] in = {{2,5},{1,2},{7,9}};
//        double[][] b = {{6,7},{8,9}};
//        double[][] forwardResult = Helper.linear_forward(in,w1,b);
//        System.out.println(Arrays.deepToString(forwardResult));

        //the following code tests the forward propagation of NN using fixed input, W, and B
//        double[][] inputLayer = {{2,5,4},{8,4,3},{5,6,2},{6,6,7},{5,0,6}};
//        double[][] w1 = {{0.1,0,0.5,0.8,0.4},{1.0,0.7,0.2,1.2,1.5},{0.9,0.5,0.5,2.1,0},{0.6,0,0.8,1.3,0.5}};
//        double[][] w2 = {{1,0.9,0.5,0.2},{0.4,0.3,0.9,0.4},{0.8,0,0.2,0.6}};
//        double[][] b1 = {{0.1},{0},{0.2},{0.1}};
//        double[][] b2 = {{0.2},{0.3},{0.1}};

//        FullyConnectedNN NN = new FullyConnectedNN(W,B,inputLayer, Y);
//        MiniBatch NN = new MiniBatch(layerSizes, inputLayer, Y,1, 200);

//        double[][] AL = NN.forward_prop();
//        double[][] AL = NN.train(0.03);
//        System.out.println(Arrays.deepToString(AL));
//        Helper.serializeNN(NN,"testSerialize.txt");
//        Helper.accuracy(AL, Y);
//        plotCost("NNTest plot", NN.getCost(), NN.getShowCost());

        // Test if the program can load one image of different size into FeatureLayer
        Preprocessing preprocessing = new Preprocessing("testProcessImage.jpg", Option.Grayscale, false);
        FeatureLayer featureLayer = preprocessing.getFeatureLayer();
        FeatureBlock testResize = featureLayer.get(0);
        testResize.showImage("Original", Option.Grayscale);

    }
}
