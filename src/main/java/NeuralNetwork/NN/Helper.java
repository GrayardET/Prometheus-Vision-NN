package NeuralNetwork.NN;

import NeuralNetwork.ConvNet.FeatureLayer;
import NeuralNetwork.ConvNet.Kernel;
import NeuralNetwork.math;
import Processing.Option;
import Processing.Preprocessing;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Helper {

    /**
     * One step forward propagation
     * @param A_prev previous activation output layer
     * @param w weights (two dim)
     * @param b bias of shape(n,1)
     * @return a layer ready for activation
     */
    public static double[][] linear_forward(double[][] A_prev, double[][] w, double[][] b){
        double[][] temp = math.multiply(w,A_prev);
        return math.add(temp, b);
    }

    /**
     * Compute the cost of NN
     * AL of shape(n,1), Y of shape (n)
     * @param AL output of softmax function
     * @param Y true label of images from preprocessing
     * @return A number.
     */
    public static double cost(double[][] AL, double[][] Y){
        double result=0;
        for(int i=0;i<AL.length; i++){
            for(int j=0; j<AL[0].length; j++){
                result += (-Math.log(AL[i][j])*Y[i][j]);
            }
        }
        return result/(Y[0].length);
    }

    /**
     * Compute the cost of NN with L2 regularization
     * @param AL output of softmax function
     * @param Y
     * @param lambda
     * @return
     */
    public static double cost(double[][] AL, double[][] Y, ArrayList<double[][]> W, double lambda){
        double crossEntropy_cost = 0;
        double regularization_cost = 0;
        double m = Y[0].length;
        for(int i=0;i<AL.length; i++){
            for(int j=0; j<AL[0].length; j++){
                crossEntropy_cost += (-Math.log(AL[i][j])*Y[i][j]);
            }
        }

        for(double[][] w:W){
            regularization_cost += math.sum_squared(w);
        }

        return crossEntropy_cost/m + lambda/(2*m)*regularization_cost;
    }


    public static double[][] negToZero(double[][] dZ, double[][] Z){
        int height = dZ.length;
        int width = dZ[0].length;
        assert height == Z.length;
        assert width == Z[0].length;

        for(int i=0; i<height; i++){
            for(int j=0; j< width; j++){
                if (Z[i][j]<=0) {
                    dZ[i][j] = 0;
                }
            }
        }

        return dZ;
    }


    public static double[] toLabel(double[] input){
        double[] result = new double[input.length];
        double max = input[0];
        int position = 0;
        for (int i = 1; i < input.length; i++) {
            if (input[i] > max) {
                max = input[i];
                position = i;
            }
        }
        for (int i = 0; i < input.length; i++) {
            if(i == position)
                result[i] = 1;
            else
                result[i] = 0;
        }
        return result;
    }

    /**
     * This method tests the accuracy of the neural network
     * @param AL
     * @param Y
     */
    public static void accuracy(double[][] AL, double[][] Y){
        double[][] AL_T = math.T(AL);
        double[][] Y_T = math.T(Y);
        int h = AL_T.length;
        int w = AL_T[0].length;
        double[] label1 = {1.0,0.0,0.0,0.0}; // open
        double[] label2 = {0.0,1.0,0.0,0.0}; // stair
        double[] label3 = {0.0,0.0,1.0,0.0}; // room
        double[] label4 = {0.0,0.0,0.0,1.0}; // hall
        double total_Correct = 0, room = 0, room_Correct=0, open =0, open_Correct = 0, stair=0, stair_correct=0,
                hall =0, hall_Correct = 0;

        for (int i = 0; i < h; i++) {
            double[] temp = Arrays.copyOf(AL_T[i],AL_T[i].length);
            double[] label = Arrays.copyOf(Y_T[i], Y_T[i].length);
            if(Arrays.equals(label, label1)){
                open++;
                if (Arrays.equals(label, toLabel(temp))) {
                    open_Correct++;
                    total_Correct++;
                }
            }else if (Arrays.equals(label, label2)){
                stair++;
                if(Arrays.equals(label, toLabel(temp))){
                    stair_correct++;
                    total_Correct++;
                }
            }else if (Arrays.equals(label,label3)){
                room++;
                if(Arrays.equals(label, toLabel(temp))){
                    room_Correct++;
                    total_Correct++;
                }
            }else{
                hall++;
                if(Arrays.equals(label, toLabel(temp))){
                    hall_Correct++;
                    total_Correct++;
                }
            }
        }
        System.out.println("Accuracy for 'open': %" + (open_Correct/open*100.0));
        System.out.println("Accuracy for 'stair': %" + (stair_correct/stair*100.0));
        System.out.println("Accuracy for 'room': %" + (room_Correct/room*100.0));
        System.out.println("Accuracy for 'hall': %" + (hall_Correct/hall*100.0));
        System.out.println("#total test images: " + h);
        System.out.println("#Total Accuracy: " + (total_Correct/h*100.0));
    }

    /**
     * Serialize a mini batch for later use
     * @param NN mini-batch
     * @param path path to save the file
     * @throws IOException
     */
    public static void serializeNN(MiniBatch NN, String path) throws IOException {
        FileOutputStream writeFile = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(writeFile);
        out.writeObject(NN);
        out.close();
        writeFile.close();
    }

    /**
     * load the saved mini batch
     * @param path
     * @return a Minibatch object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static MiniBatch loadNN(String path) throws IOException, ClassNotFoundException {
        FileInputStream readFile = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(readFile);
        MiniBatch loadedNN = (MiniBatch)in.readObject();
        in.close();
        readFile.close();
        return loadedNN;
    }

    /**
     * Save all the parameters after training for later predictions and the democratic algo
     * @param NN mini batch
     * @param path
     * @throws IOException
     */
    public static void serializeParameters(NeuralNetwork NN, String path) throws IOException{
        Parameters parameters = new Parameters(NN.getW(),NN.getB());
        FileOutputStream writeFile = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(writeFile);
        out.writeObject(parameters);
        out.close();
        writeFile.close();
    }

    /**
     * laod parameters from disk
     * @param path
     * @return Parameters object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Parameters loadSerializedParameters(String path) throws IOException, ClassNotFoundException {
        FileInputStream readFile = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(readFile);
        Parameters result = (Parameters)in.readObject();
        in.close();
        readFile.close();
        return result;
    }




}
