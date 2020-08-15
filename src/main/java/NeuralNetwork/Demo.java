package NeuralNetwork;

import NeuralNetwork.ConvNet.*;
import Processing.Option;
import Processing.Preprocessing;

import java.util.ArrayList;

public class Demo {
    public static void main(String[] args) throws Exception {

        /**
         * Demo 1
         * Preprocessing
         * Loading images
         * plot both the one original and it's flipped image
         */

        Preprocessing  preprocessing =  new Preprocessing("trainingDemo\\", Option.Grayscale,true);
        FeatureLayer ImageDemo = preprocessing.getFeatureLayer();
        FeatureBlock OriginalImg = ImageDemo.get(0);

        OriginalImg.showImage("Original",Option.Grayscale);


        /**
         * Demo 2
         * Feature Extraction
         * 1.Extract features by custom designed kernels
         * 2.Show the result
         */
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

        ImageDemo.extract(kernel1);
        ImageDemo.extract(kernel2);
//        ImageDemo.pooling(4);

        OriginalImg = ImageDemo.get(0);
//        OriginalImg.showImage("Original", Option.Grayscale);
//        OriginalImg.showImage("Original_EdgeDetection", Option.Grayscale);
//        OriginalImg.showImage("Original_GaussianBlur_EdgeDetection", Option.Grayscale);
//        OriginalImg.showImage("Original_GaussianBlur_Sharpen", Option.Grayscale);
        OriginalImg.showImage("Original_SobelVertical", Option.Grayscale);
        OriginalImg.showImage("Original_Horizontal", Option.Grayscale);
        OriginalImg.showImage("Original_Cross", Option.Grayscale);
    }
}
