package NeuralNetwork.ConvNet;

import java.util.Arrays;
import java.util.HashMap;

public class Kernel {

    static final double[][] vertical = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
    static final double[][] sobel_vertical = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
    static final double[][] scharr_vertical = {{3, 0, -3}, {10, 0, -10}, {3, 0, -3}};
    static final double[][] horizontal= {{1, 1, 1},{0, 0, 0},{-1, -1, -1}};
    static final double[][] scharr_horizontal = {{3, 10, 3}, {0, 0, 0}, {-3, -10, -3}};
    static final double[][] sharpen = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
    static final double[][] gaussian_blur = {{(1.0/16.0),(2.0/16.0),(1.0/16.0)},{(2.0/16.0),(4.0/16.0),(2.0/16.0)}, {(1.0/16.0),(2.0/16.0),(1.0/16.0)}};
    static final double[][] edge_detection = {{-1,-1,-1},{-1,8, -1},{-1, -1,-1}};
    static final double[][] box_blur = {{1.0/9.0,1.0/9.0,1.0/9.0},{1.0/9.0,1.0/9.0,1.0/9.0},{1.0/9.0,1.0/9.0,1.0/9.0}};
    static final double[][] cross = {{1.0,-1.0,1.0},{-1.0,1.0,-1.0},{1.0,-1.0,1.0}};
    static final double[][] edge_detection2 = {{1,0,-1},{0,0,0},{-1,0,1}};
    static final double[][] edge_detection3 = {{0,-1,0},{-1,4,-1},{0,-1,0}};


    public static final Kernel Sharpen = new Kernel("Sharpen", sharpen);
    public static final Kernel Vertical = new Kernel("Vertical", vertical);
    public static final Kernel SobelVertical = new Kernel("SobelVertical", sobel_vertical);
    public static final Kernel ScharrVertical = new Kernel("ScharrVertical", scharr_vertical);
    public static final Kernel Horizontal = new Kernel("Horizontal", horizontal);
    public static final Kernel ScharrHorizontal = new Kernel("ScharrHorizontal", scharr_horizontal);
    public static final Kernel BoxBlur = new Kernel("BoxBlur", box_blur);
    public static final Kernel GaussianBlur = new Kernel("GaussianBlur", gaussian_blur);
    public static final Kernel EdgeDetection = new Kernel("EdgeDetection", edge_detection);
    public static final Kernel EdgeDetection2 = new Kernel("edgeDetection2", edge_detection2);
    public static final Kernel EdgeDetection3 = new Kernel("edgeDetection3", edge_detection3);
    public static final Kernel Cross = new Kernel("Cross", cross);

    private double[][] kernel;
    private String name;


    /**
     * construct a new kernel
     * @param name name of Kernel
     * @param kernelValue Actual matrix representing kernel
     * @pre kernelHeight = kernelWidth
     * @pre kernel size is odd(for padding)
     */
    public Kernel(String name, double[][] kernelValue){
        int h = kernelValue.length;
        int w = kernelValue[0].length;
        assert h == w;
        if (h%2 == 0) {
            System.out.println("Please initialize kernel with odd size");
            System.exit(-1);
        }
        this.kernel = Arrays.copyOf(kernelValue, kernelValue.length);
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public double[][] getKernel(){
        return Arrays.copyOf(kernel, kernel.length);
    }

    public int getKernelSize(){
        return this.kernel.length;
    }
}
