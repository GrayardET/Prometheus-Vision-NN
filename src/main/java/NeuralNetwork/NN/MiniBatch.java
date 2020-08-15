package NeuralNetwork.NN;

import NeuralNetwork.ConvNet.FeatureLayer;
import NeuralNetwork.math;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MiniBatch implements Serializable{
            private ArrayList<double[][]> W = new ArrayList<>();
            private ArrayList<double[][]> B = new ArrayList<>();
            private ArrayList<double[][]> cache_Z = new ArrayList<>();
            private ArrayList<double[][]> cache_A = new ArrayList<>();
            private ArrayList<double[][]> dW = new ArrayList<>(W.size());
            private ArrayList<double[][]> dB = new ArrayList<>(B.size());
            private ArrayList<double[][]> VdW = new ArrayList<>();
            private ArrayList<double[][]> VdB = new ArrayList<>();
            private ArrayList<double[][]> miniBatches = new ArrayList<>();
            private int showCost = 0;
            private double[][] Y;
            private double[][] inputLayer = null;
            private double lambda = 0;
            private FeatureLayer featureLayer;
            private ArrayList<Double> cost = new ArrayList<>();
            private double beta;


    // for prediction using mini batch
    public MiniBatch(ArrayList<double[][]> W, ArrayList<double[][]> B, double[][] input){
        this.W = W;
        this.B = B;
        this.inputLayer = math.normalize(input);
    }

    //NN with regularization
//    public MiniBatch(double lambda, int[] layerSizes, FeatureLayer inputLayer, double[][] Y, int seed, int showCost, double... beta){
//        this.inputLayer = math.normalize(inputLayer.flatten());
//        int firstLayerSize = inputLayer.getBlockSize();
//        int length = layerSizes.length;
//        this.showCost = showCost;
//        this.featureLayer = inputLayer;
//        this.Y = Y;
//        this.lambda = lambda;
//        if(beta.length != 0) {
//            assert 0<beta[0] && beta[0] <=1;
//            this.beta = beta[0];
//            VdW.add(math.multiply(2.0/(this.inputLayer.length),math.zero(layerSizes[0],firstLayerSize * 3)));
//            VdB.add(math.multiply(2.0/(this.inputLayer.length),math.zero(layerSizes[0],1)));
//        }
//        //create W and b for input layer
//        W.add(math.multiply(2.0/(this.inputLayer.length),math.random(layerSizes[0],firstLayerSize * 3,seed)));
//        B.add(math.multiply(2.0/(this.inputLayer.length),math.random(layerSizes[0],1,seed)));
//        for (int l = 0; l < length-1; l++) {
//            int dim_cur = layerSizes[l];
//            int dim_next = layerSizes[l + 1];
//            W.add(math.multiply(2.0/(dim_cur),math.random(dim_next, dim_cur,seed+1)));
//            B.add(math.multiply(2.0/(dim_cur),math.random(dim_next, 1,seed+1)));
//            if(beta.length != 0){
//                VdW.add(math.multiply(2.0/(dim_cur),math.zero(dim_next, dim_cur)));
//                VdB.add(math.multiply(2.0/(dim_cur),math.zero(dim_next, 1)));
//            }
//        }
//
//        // initialize dW and dB
//        for(int i = 0; i < W.size(); i++){
//            dW.add(null);
//            dB.add(null);
//        }
//    }

    //for mini batch
    public MiniBatch(ArrayList<double[][]> W, ArrayList<double[][]> B, double[][] input, double[][] Y){
        this.W = W;
        this.B = B;
        this.inputLayer = math.normalize(input);
        this.Y = Y;

        for(int i = 0; i < W.size(); i++) {
            dW.add(null);
            dB.add(null);
        }
    }

    //for mini batch
    public MiniBatch(ArrayList<double[][]> W, ArrayList<double[][]> B,ArrayList<double[][]> VdW, ArrayList<double[][]> VdB,
                     double[][] input, double[][] Y, double beta){
        this.W = W;
        this.B = B;
        this.inputLayer = math.normalize(input);
        this.Y = Y;
        this.VdB = VdB;
        this.VdW = VdW;
        this.beta = beta;
        for(int i = 0; i < W.size(); i++) {
            dW.add(null);
            dB.add(null);
        }
    }
    //for prediction(2)
    public MiniBatch(String parametersPath, FeatureLayer featureLayer) throws IOException, ClassNotFoundException {
        Parameters p = Helper.loadSerializedParameters(parametersPath);
        this.W = p.getW();
        B = p.getB();
        this.featureLayer = featureLayer;
        this.inputLayer = math.normalize(featureLayer.flatten());
    }


    //for testing purpose
        public MiniBatch(int[] layerSizes, double[][] inputLayer, double[][] Y, int seed, int showCost, double... beta){
        this.inputLayer = math.normalize(inputLayer);
        int firstLayerSize = inputLayer.length;
        int length = layerSizes.length;
        this.showCost = showCost;
        this.Y = Y;
        //create W and b for input layer
        W.add(math.multiply(2.0/(inputLayer.length),math.random(layerSizes[0],firstLayerSize,seed)));
        B.add(math.multiply(2.0/(inputLayer.length),math.random(layerSizes[0],1,seed)));
        if(beta.length != 0) {
            assert 0<beta[0] && beta[0] <=1;
            this.beta = beta[0];
            VdW.add(math.multiply(2.0/(this.inputLayer.length),math.zero(layerSizes[0],firstLayerSize)));
            VdB.add(math.multiply(2.0/(this.inputLayer.length),math.zero(layerSizes[0],1)));
        }
        for (int l = 0; l < length-1; l++) {
            int dim_cur = layerSizes[l];
            int dim_next = layerSizes[l + 1];
            W.add(math.multiply(2.0/(dim_cur),math.random(dim_next, dim_cur,seed+1)));
            B.add(math.multiply(2.0/(dim_cur),math.random(dim_next, 1,seed+1)));
            if(beta.length != 0){
                VdW.add(math.multiply(2.0/(dim_cur),math.zero(dim_next, dim_cur)));
                VdB.add(math.multiply(2.0/(dim_cur),math.zero(dim_next, 1)));
            }
        }

        // initialize dW and dB
        for(int i = 0; i < W.size(); i++){
            dW.add(null);
            dB.add(null);
        }
    }


    public ArrayList<double[][]> getW() {
        return W;
    }

    public ArrayList<double[][]> getB() {
        return B;
    }

    public double[][] getY(){
        return Arrays.copyOf(Y, Y.length);
    }

    public FeatureLayer getFeatureLayer() {
        return featureLayer;
    }

    public void appendCost(double a){
        cost.add(a);
    }

    public ArrayList<Double> getCost(){
        return this.cost;
    }

    public int getShowCost(){
        return this.showCost;
    }

    public void clearCaches(){
        this.cache_A = new ArrayList<>();
        this.cache_Z = new ArrayList<>();
    }

    /**
     * Forward Propagation, puts all Activation layer in cache
     * @return Softmax result of the last layer
     */
    public double[][] forward_prop() {
        //implement linear -> ReLu
        int length = W.size();
        double[][] A_prev = inputLayer;
        for (int i = 0; i < length-1; i++) {
            double[][] Z = Helper.linear_forward(A_prev, W.get(i), B.get(i));
            cache_Z.add(Z);

            double[][] A = math.relu(Z);
            cache_A.add(A);
            A_prev = A;
        }

        //implement linear -> softmax
        double[][] Z = Helper.linear_forward(A_prev, W.get(length-1), B.get(length-1));
        cache_Z.add(Z);
        double[][] AL = math.softmax(Z);
        cache_A.add(AL);
        return AL;
    }

    public double[] predict_Democratic(){
        int length = W.size();
        double[][] A_prev = inputLayer;
        for (int i = 0; i < length-1; i++) {
            double[][] Z = Helper.linear_forward(A_prev, W.get(i), B.get(i));
            cache_Z.add(Z);

            double[][] A = math.relu(Z);
            cache_A.add(A);
            A_prev = A;
        }

        //implement linear -> softmax
        double[][] Z = Helper.linear_forward(A_prev, W.get(length-1), B.get(length-1));
        cache_Z.add(Z);
        double[][] AL = math.relu(Z);
        double[] result = math.getFirstColumn(AL);
        return result;
    }

    /**
     * back propagation that modifies W and B
     * @param AL output layer of softmax function
     * @param learningRate for updating W and B
     */
    public void backward_prop(double[][] AL, double learningRate){


        // initialize all parameters for softmax layer
        double[][] dZ = math.subtract(AL, Y);
        double[][] A_prev = cache_A.get(cache_A.size()-2);
        double[][] w = W.get(W.size()-1);
        int dW_size = dW.size();
        int dB_size = dB.size();
        double m = A_prev[0].length;

        assert dZ[0].length == math.T(A_prev).length;
        double[][] dw = null;
        if(this.lambda != 0){
            dw = math.add(math.multiply(1.0/m, math.multiply(dZ,math.T(A_prev))), math.multiply(lambda/m,W.get(W.size()-1)));
        }else{
            dw = math.multiply(1.0/m, math.multiply(dZ,math.T(A_prev)));
        }
        dW.set(dW_size-1, dw);

        double[][] db = math.multiply(1.0/m, math.add_Horizontal(dZ));
//        double[][] db = Arrays.copyOf(dZ,dZ.length);
        dB.set(dB_size-1, db);

        assert w.length == dZ.length;
        double[][] dA_prev = math.multiply(math.T(w), dZ);

        // implement back_prop for ReLu function for all previous layers.
        for(int i=W.size()-2;i>=0;i--){
            double[][] dA = Arrays.copyOf(dA_prev,dA_prev.length);
            dZ = Arrays.copyOf(dA, dA.length);

            if(i !=0)
                A_prev = cache_A.get(i-1);
            else
                A_prev = Arrays.copyOf(inputLayer,inputLayer.length);

            double[][] Z = cache_Z.get(i);
            dZ = Helper.negToZero(dZ, Z);

            assert dZ[0].length == math.T(A_prev).length;
            if(this.lambda != 0) {
                dw = math.add(math.multiply(1.0 / m, math.multiply(dZ, math.T(A_prev))), math.multiply(lambda / m, W.get(i)));
            }else{
                dw = math.multiply(1.0 / m, math.multiply(dZ, math.T(A_prev)));
            }
            dW.set(i, dw);

            db = math.multiply(1.0/m, math.add_Horizontal(dZ));
//            db = Arrays.copyOf(dZ,dZ.length);
            dB.set(i, db);

            w = W.get(i);
            assert w.length == dZ.length;
            dA_prev = math.multiply(math.T(w), dZ);
        }

        update(learningRate);
        clearCaches();
    }

    public void update(double learningRate){
        assert W.size() == dW.size();
        assert B.size() == dB.size();
        assert W.size() == B.size();
        int length = W.size();
        for(int i=0; i<length; i++){
            double[][] tempW = W.get(i);
            double[][] tempB = B.get(i);
            if(beta == 0) {
                tempW = math.subtract(tempW, math.multiply(learningRate, dW.get(i)));
                tempB = math.subtract(tempB, math.multiply(learningRate, dB.get(i)));
                this.W.set(i, tempW);
                this.B.set(i, tempB);
            }else{
                double[][] tempVdw = VdW.get(i);
                double[][] tempVdb = VdB.get(i);
                tempVdw = math.add(math.multiply(beta,tempVdw),math.multiply(1-beta, dW.get(i)));
                tempVdb = math.add(math.multiply(beta,tempVdb),math.multiply(1-beta, dB.get(i)));
                // update parameters stored in VdW and VdB
                VdW.set(i, tempVdw);
                VdB.set(i, tempVdb);
                double[][] a = math.subtract(tempW, math.multiply(learningRate, tempVdw));
                double[][] b = math.subtract(tempB, math.multiply(learningRate, tempVdb));
                this.W.set(i, a);
                this.B.set(i, b);
            }
        }
    }


    public void train(double learningRate){

        double[][] AL = this.forward_prop();
        double curCost;
        if (lambda != 0) {
            curCost = Helper.cost(AL, Y, this.W, this.lambda);
        }else {
            curCost = Helper.cost(AL,Y);
        }
        System.out.println("Cost = " + curCost);

        this.backward_prop(AL, learningRate);
    }




}
