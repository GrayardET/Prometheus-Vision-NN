package NeuralNetwork.ConvNet;

import Processing.ImageHandler;
import Processing.Option;

import java.io.Serializable;
import java.util.*;

/**
 * In this class, every KeyPair of the HashMap represents a version of the Image(original, Blurred, sharpened, Edge detection, etc)
 * HashMap<String, int[][]> block:
 * String -- name, of the form Original_(kernels you applied on it in the correct order)
 * int[][] -- integer array representing the actual pixel value of the image
 * Example: Applying kernel Kernel.Sharpen on the original image produces a new key pair <Original_Sharpen, int[][]>
 * Every new produced key pair will be appended on Block
 *
 *
 * A filter can only be applied once on each version, meaning Original_Sharpen_Sharpen, etc, are not allowed
 * Permutation of a version is not allowed
 * meaning that Original_Vertical_Horizontal and Original_Horizontal_Vertical cannot both exist in the HashMap.
 * In order to maintain the size of each image for dense layer, pooling applies to all images in the block
 */

public class FeatureBlock implements Serializable {
    HashMap<String, int[][]> block = new HashMap<>();

    public FeatureBlock(){};
    public FeatureBlock(String name,int[][] value){
        block.put(name, Arrays.copyOf(value, value.length));
    }


    /**
     * If you haven't read the class description, please do so.
     * Convolve a list of kernels with every existing image in this.block
     * Append new versions of image to this.block, while keeping the old ones.
     * @param kernels != null && kernels.size() != 0;
     */
    public void extract(ArrayList<Kernel> kernels) {

        assert kernels != null && kernels.size() != 0;
        int kernelSize = kernels.get(0).getKernelSize();

        /*
        Note: Modifying the HashMap while iterating through it will throw ConcurrentModificationException.
        To avoid the exception, create a new HashMap to keep track of the changes
        then append them after Iteration is over.
         */

        HashMap<String, int[][]> result= new HashMap<>();
        Set<Map.Entry<String, int[][]>> value = block.entrySet();

        // loop through every image in featureBlock
        for (Map.Entry<String, int[][]> slice: value) {
            int[][] sliceValue = padding(slice.getValue(), (kernelSize-1)/2);
            int blockHeight = sliceValue.length;
            int blockWidth = sliceValue[0].length;
            int newHeight = blockHeight - kernelSize + 1;
            int newWidth = blockWidth - kernelSize + 1;

            // traverse through every kernel in the ArrayList.
            for (Kernel kernel : kernels) {
                if(!block.containsKey(kernel.getName()) && !slice.getKey().contains(kernel.getName()))
                    result.put(slice.getKey() + "_" + kernel.getName(), conv_forward(newHeight, newWidth, sliceValue, kernel));
            }
        }
        // update this.block
        update(result);
    }

    /**
     * This method allows user to apply kernels to a specific version of image
     * @param key a specific version of image being edited
     * @param kernels kernels
     * @throws Exception Cannot find key
     * @Pre kernels != null && kernels.size != 0
     */
    public void extract(String key, ArrayList<Kernel> kernels) throws Exception{
        assert kernels != null && kernels.size() != 0;
        int kernelSize = kernels.get(0).getKernelSize();
        HashMap<String, int[][]> result= new HashMap<>();
        // check if the HashMap contains the key provided
        if (!block.containsKey(key)) {
            throw new Exception ("Cannot find the provided when extracting");
        }

        int[][] sliceValue = padding(block.get(key), (kernelSize-1)/2);
        int blockHeight = sliceValue.length;
        int blockWidth = sliceValue[0].length;
        int newHeight = blockHeight - kernelSize + 1;
        int newWidth = blockWidth - kernelSize + 1;

        for(Kernel kernel: kernels){
            result.put(key + "_" + kernel.getName(), conv_forward(newHeight, newWidth, sliceValue, kernel));
        }

        update(result);
    }

    /**
     * Convolve the given slice(image) with the kernel, producing a new image
     * @param newHeight height of the new Image
     * @param newWidth width of the new Image
     * @param sliceValue int[][] representing pixel values of the current version of image.
     * @param kernel kernel
     * @return a new image in int[][]
     */
    private int[][] conv_forward(int newHeight, int newWidth, int[][] sliceValue, Kernel kernel){
        int kernelSize = kernel.getKernel().length;
        int[][] temp = new int[newHeight][newWidth];

        // traverse through image
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                double R = 0, G = 0, B = 0, A = 0;
                int h_start = h;
                int h_end = h_start + kernelSize;
                int w_start = w;
                int w_end = w_start + kernelSize;

                //traverse through kernel
                for (int i = 0; i < kernelSize; i++) {
                    for (int j = 0; j < kernelSize; j++) {
                        double kernel_val = kernel.getKernel()[i][j];
                        int slice_val = sliceValue[h + i][w + j];
                        if (kernel_val != 0) {
                            A += kernel_val * ((slice_val >> 24) & 0xff);
                            R += kernel_val * ((slice_val >> 16) & 0xff);
                            G += kernel_val * ((slice_val >> 8) & 0xff);
                            B += kernel_val * ((slice_val) & 0xff);
                        }
                    }
                    // not sure about the +0.5, inspired by last year's student
                    int R_final = round(R + 0.5);
                    int G_final = round(G + 0.5);
                    int B_final = round(B + 0.5);
                    int A_final = round(A + 0.5);
                    temp[h][w] = (A_final << 24) | (R_final) << 16 | (G_final << 8) | (B_final);
                }
            }
        }
        return temp;
    }

    /**
     * Append new added versions to this.block
     * @param input A Hashmap that keeps track of the changes.
     */
    private void update(HashMap<String,int[][]> input){
        Set<Map.Entry<String, int[][]>> resultEntry = input.entrySet();
        for(Map.Entry<String, int[][]> a: resultEntry){
            this.block.put(a.getKey(),a.getValue());
        }
    }

    /**
     * rounds double to int value
     * @param a value
     * @return int
     */
    private static int round(double a){
        return (int)((a<0) ? 0: (a>255) ? 255: a);
    }


    public int getSize(){
        return this.block.size();
    }

    public int getBlockSize(){
        int size = this.block.size();
        int[][] slice = this.block.get("Original");
        int h = slice.length;
        int w = slice[0].length;
        int result = size * h * w;
        return result;
    }

//    public void showImage(int i, Option option){
//        int[][] temp = this.block.get(i);
//        int height = temp.length;
//        int width = temp[0].length;
//        ImageHandler.show(temp, height, width, Option.Grayscale);
//    }


    public void showImage(String name, Option option) throws Exception{
        int[][] temp = block.get(name);
        if(temp == null)
            throw new Exception ("Cannot find the image specified.");

        int height = temp.length;
        int width = temp[0].length;
        ImageHandler.show(temp, height, width, option);
    }

    /**
     * method applies max pooling on every version of image inside this.block
     * @param size size of pooling kernel
     */
    public void pooling(int size){
        Set<Map.Entry<String, int[][]>> value = block.entrySet();
        for(Map.Entry<String, int[][]> slice: value){
            int[][] image = slice.getValue();
            slice.setValue(shrink(image, size));
        }
    }

    /**
     * Helper method for pooling
     * @param slice image
     * @param size pooling kernel size
     * @return new int[][] after pooling
     */
    private static int[][] shrink(int[][] slice,int size){
        int h = slice.length;
        int w = slice[0].length;
        int new_h = h/size;
        int new_w = w/size;
        int[][] result = new int[new_h][new_w];
        for(int i=0; i< new_h; i++){
            for (int j=0; j<new_w; j++) {
                int x = i * size;
                int x_end = x+size;
                int y = j * size;
                int y_end = y+size;
                int max = slice[x][y];
                //find max value
                for(; x<x_end; x++){
                    for(; y<y_end; y++){
                        max = Math.max(max, slice[x][y]);
                    }
                }
                result[i][j] = max;
            }
        }
        return result;
    }

    /**
     * Flatten the block into one single array
     * Each version of image is separated into three channels --> R, G, B
     * @return A one dimension Array representing all three channels of the entire block.
     */
    public double[] flatten(){
        int size = block.size();
        int[][] temp = block.values().iterator().next();
        int h = temp.length;
        int w = temp[0].length;
        double[] result = new double[size*h*w*3];
        int count=0;
        Set<Map.Entry<String, int[][]>> value = block.entrySet();
        for(Map.Entry<String, int[][]> slice : value){
            int[][] val = slice.getValue();
            for(int c =0; c<3; c++) {
                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        if(c == 0) {
                            result[count] = ((val[i][j]>>16) & 0xff);
                        }else if(c==1){
                            result[count] = ((val[i][j]>>8) & 0xff);
                        }else{
                            result[count] = ((val[i][j]) & 0xff);
                        }
                        count++;
                    }
                }
            }
        }
        return result;
    }


    /**
     * Adds padding to preserve information of the image's edges
     * @param original matrix that represents the original image
     * @param a padding width
     * @return int[][] representing an image after padding
     */
    public static int[][] padding(int[][] original, int a){
            int x=0, y=0;
            int new_h = original.length + 2*a;
            int new_w = original[0].length + 2*a;
            int[][] result = new int[new_h][new_w];
            for (int i = 0; i < new_h; i++) {
                for (int j = 0; j < new_w; j++) {
                    if(i<a || i> new_h-a-1 || j<a || j> new_w-a-1){
                        result[i][j] = 0;
                    }else{
                        result[i][j] = original[x][y];
                        y++;
                    }
                }
                if(y == original[0].length)
                x++;
                y=0;
            }
            return result;
    }

}
