package NeuralNetwork.ConvNet;

import NeuralNetwork.math;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This Class contains all the images needed to be fed into the dense layers
 * Each position in this.featureBlocks represents an original image and all other versions after convolving with kernels
 */
public class FeatureLayer implements Serializable {
    private ArrayList<FeatureBlock> featureBlocks = new ArrayList<>();

    public FeatureLayer(){}

    public void extract(ArrayList<Kernel> kernels){
        int count = 0;
        for(FeatureBlock featureBlock: featureBlocks){
            System.out.println("Extracting features of image " + count);
            featureBlock.extract(kernels);
            count++;
        }
    }

    public FeatureBlock get(int i){
        return featureBlocks.get(i);
    }

    // no clone
    public void add(FeatureBlock a){
        this.featureBlocks.add(a);
    }

    public int size(){
        return this.featureBlocks.size();
    }

    public void pooling(int a){
        for(FeatureBlock featureBlock: featureBlocks){
            featureBlock.pooling(a);
        }
    }


    /**
     * turn the FeatureLayer into a two dimensional array for dense layers
     * @return two dim array
     */
    public double[][] flatten(){
        double[][] result = new double[featureBlocks.size()][];
        int count=0;
        for(FeatureBlock featureBlock: featureBlocks){
            // might experience reference issue
            result[count] = featureBlock.flatten();
            count++;
        }
        return math.T(result);
    }

    public double[][] flatten(int start, int end){
        double[][] result = new double[end-start][];
        int count = 0;
        for(int i = start; i<end; i++){
            result[count] = featureBlocks.get(i).flatten();
            count++;
        }
        return math.T(result);
    }

    public int getBlockSize(){
        return this.featureBlocks.get(0).getBlockSize();
    }

}
