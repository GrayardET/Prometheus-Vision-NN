package NeuralNetwork.NN;

import NeuralNetwork.ConvNet.FeatureLayer;
import NeuralNetwork.ConvNet.Kernel;
import Processing.Option;
import Processing.Preprocessing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class Parameters implements Serializable, Algorithm {
    private ArrayList<double[][]> W = new ArrayList<>();
    private ArrayList<double[][]> B = new ArrayList<>();



    public Parameters(ArrayList<double[][]> W, ArrayList<double[][]> B){
        this.W = W;
        this.B = B;

    }

    public Parameters(){};


    public ArrayList<double[][]> getW(){
        return this.W;
    }
    public ArrayList<double[][]> getB(){
        return this.B;
    }

    /**
     * load a specific Parameters object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void load() throws IOException, ClassNotFoundException {
        FileInputStream readFile = new FileInputStream("SavedParameters\\4.txt");
        ObjectInputStream in = new ObjectInputStream(readFile);
        Parameters result = (Parameters)in.readObject();
        in.close();
        readFile.close();
        this.B = result.getB();
        this.W = result.getW();
    }

    /**
     * This method makes a prediction on one specific image, it serves for the democratic algorithm
     * @param imagePath path of image to be classified
     * @return a prediction
     * @throws Exception
     */
    @Override
    public double[] processImage(String imagePath) throws Exception {
        Preprocessing preprocessing = new Preprocessing(imagePath, Option.Grayscale, false);

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
        FeatureLayer featureLayer = preprocessing.getFeatureLayer();
        featureLayer.extract(kernel1);
        featureLayer.pooling(5);
        featureLayer.extract(kernel2);
        featureLayer.pooling(5);
        Parameters a = new Parameters();
        a.load();

        NeuralNetwork neuralNetwork = new NeuralNetwork(a,featureLayer);

        double[] result = neuralNetwork.predict_Democratic();
        return result;

    }
}
