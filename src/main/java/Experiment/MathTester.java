package Experiment;

import NeuralNetwork.math;


import java.util.Arrays;

public class MathTester {
    public static void main(String args[]){
        double[][] a = {{1,2},{3,4}};
        double[][] b = {{2,6},{3,5}};
        double[][] c = math.multiply(a,b);
        double[][] d = math.multiply(2, a);
        double[][] e = {{5},{2},{-1},{3}};
        double[][] f = {{1},{2}};
        double[][] softMax = math.softmax(e);
//        double[][] d = math.elementWise_multiply(a,b);
//        double e = math.dot(a,b);
//        double temp1 = 1.1;
//        int temp2 = (int)temp1;
//        System.out.println(temp2);
//        double temp3 = 1.0/3.0;
//        System.out.println("1/3 = " + temp3);
//
//        System.out.println("multiply:" + Arrays.deepToString(c));
//        System.out.println("elementwise_multiply:" + Arrays.deepToString(d));
//        System.out.println("dot:" + e);
//
//        double[][][] RandomFilter = new double[1][2][2];
//        Filters testRandFilter = new Filters();
//        testRandFilter = ConvHelper.randFilter(2,1,2,2);
//        System.out.println(testRandFilter.toString());
//
//        ArrayList<double[][]> temp = new ArrayList<>();
//        temp.add(a);
//        a= b;
//        b[0][1] = 5;
//        double[][] x = temp.get(0).clone();
//        x[0][0] = 9;
        System.out.println(Arrays.deepToString(a));
        System.out.println(Arrays.deepToString(b));
        System.out.println(Arrays.deepToString(c));
        System.out.println(Arrays.deepToString(d));
        System.out.println(Arrays.deepToString(softMax));

        System.out.println(Arrays.deepToString(math.add(a,f)));

//        double[] testToLabel = {10,0,8,0.5};
//        System.out.println(Arrays.toString(Helper.toLabel(testToLabel)));

//        double[] testSubtract1 = {1,0,0,0};
//        double[] testSubtract2 = {0.6,0.2,0.3,0.5};
//        System.out.println(Arrays.equals(testSubtract1, Helper.toLabel(testSubtract2)));




//        System.out.println(Arrays.deepToString(temp.get(0)));
//        double[][] y = Arrays.stream(a).map(double[]::clone).toArray(double[][]::new);

//        System.out.println(Arrays.deepToString(a));

        double[][] testNorm = {{1,3,2,4},{17,8,6,5},{9,5,6,0},{0,0,0,0}};
        System.out.println(Arrays.deepToString(math.norm(testNorm)));
        System.out.println(Arrays.deepToString(math.normalize(testNorm)));

    }






}
