package NeuralNetwork.NN;

import java.io.IOException;

public interface Algorithm {
    // Loads any saved training data into the current instance fo the algorithm
    void load() throws IOException, ClassNotFoundException;

    //Returns output values of the algorithm given the local path to an image
    double[] processImage(String imagePath) throws Exception;

}
