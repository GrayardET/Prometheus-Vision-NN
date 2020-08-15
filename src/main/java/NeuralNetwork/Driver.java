package NeuralNetwork;

import NeuralNetwork.ConvNet.*;
import NeuralNetwork.NN.Helper;
import NeuralNetwork.NN.LinePlotter;
import NeuralNetwork.NN.NeuralNetwork;
import Processing.Option;
import Processing.Preprocessing;


import java.util.ArrayList;

public class Driver {

    public static void main(String args[]) throws Exception {

        //setting up the kernels for feature extraction
        ArrayList<Kernel> kernel1 = new ArrayList<>();
        kernel1.add(Kernel.GaussianBlur);
        kernel1.add(Kernel.BoxBlur);

        ArrayList<Kernel> kernel2 = new ArrayList<>();
        kernel2.add(Kernel.EdgeDetection);
        kernel2.add(Kernel.ScharrVertical);
        kernel2.add(Kernel.ScharrHorizontal);
        kernel2.add(Kernel.Cross);
        kernel2.add(Kernel.Horizontal);
        kernel2.add(Kernel.Sharpen);
        kernel2.add(Kernel.SobelVertical);
        kernel2.add(Kernel.Vertical);
        kernel2.add(Kernel.EdgeDetection2);
        kernel2.add(Kernel.EdgeDetection3);



        Preprocessing preprocessing = new Preprocessing("training\\", Option.Grayscale,false);
//        Preprocessing preprocessing = new Preprocessing("trainingDemo\\", Option.Grayscale,  false);
        FeatureLayer trainingSet = preprocessing.getFeatureLayer();
        FeatureBlock demoImg = trainingSet.get(0);
        demoImg.showImage("Original",Option.Grayscale);



        // feature extraction
        System.out.println("Extracting features...");

        trainingSet.extract(kernel1);
        trainingSet.pooling(5);
        trainingSet.extract(kernel2);
        trainingSet.pooling(5);
        System.out.println("Feature extraction completed!");

        demoImg.showImage("Original_GaussianBlur_EdgeDetection", Option.Grayscale);


        // initialize neural network and start training
        int[] layerSizes = {10,6,4};
        NeuralNetwork neuralNetwork = new NeuralNetwork(layerSizes,trainingSet,128,1, preprocessing.getY(),0.9);
        neuralNetwork.train(0.05,2000);
        Helper.serializeParameters(neuralNetwork, "SavedParameters\\4.txt");


        // prediction:
//        Preprocessing preprocessing2 = new Preprocessing("trainingDemo\\", Option.Grayscale,false);
        Preprocessing preprocessing2 = new Preprocessing("testing\\", Option.Grayscale,false);
        FeatureLayer testingSet = preprocessing2.getFeatureLayer();
        testingSet.extract(kernel1);
        testingSet.pooling(5);
        testingSet.extract(kernel2);
        testingSet.pooling(5);
        NeuralNetwork neuralNetwork2 = new NeuralNetwork(neuralNetwork.getW(),neuralNetwork.getB(),testingSet, preprocessing2.getY());
        neuralNetwork2.predict();
    }
}
