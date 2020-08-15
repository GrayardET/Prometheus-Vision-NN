package NeuralNetwork;

import java.util.Random;

/**
 * This class is inspired by: @author Deus Jeraldy
 * @Email: deusjeraldy@gmail.com
 */


public class math {
    private static Random random;
    private static long seed;

    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    /**
     * Sets the seed of the pseudo-random number generator. This method enables
     * you to produce the same sequence of "random" number for each execution of
     * the program. Ordinarily, you should call this method at most once per
     * program.
     *
     * @param s the seed
     */
    public static void setSeed(long s) {
        seed = s;
        random = new Random(seed);
    }

    /**
     * Returns the seed of the pseudo-random number generator.
     *
     * @return the seed
     */
    public static long getSeed() {
        return seed;
    }

    /**
     * Returns a random real number uniformly in [0, 1).
     *
     * @return a random real number uniformly in [0, 1)
     */
    public static double uniform() {
        return random.nextDouble();
    }

    /**
     * Returns a random integer uniformly in [0, n).
     *
     * @param n number of possible integers
     * @return a random integer uniformly between 0 (inclusive) and {@code n}
     * (exclusive)
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public static int uniform(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("argument must be positive: " + n);
        }
        return random.nextInt(n);
    }

    /**
     * Returns a random long integer uniformly in [0, n).
     *
     * @param n number of possible {@code long} integers
     * @return a random long integer uniformly between 0 (inclusive) and
     * {@code n} (exclusive)
     * @throws IllegalArgumentException if {@code n <= 0}
     */
    public static long uniform(long n) {
        if (n <= 0L) {
            throw new IllegalArgumentException("argument must be positive: " + n);
        }

        long r = random.nextLong();
        long m = n - 1;

        // power of two
        if ((n & m) == 0L) {
            return r & m;
        }

        // reject over-represented candidates
        long u = r >>> 1;
        while (u + m - (r = u % n) < 0L) {
            u = random.nextLong() >>> 1;
        }
        return r;
    }

    /**
     * Returns a random integer uniformly in [a, b).
     *
     * @param a the left endpoint
     * @param b the right endpoint
     * @return a random integer uniformly in [a, b)
     * @throws IllegalArgumentException if {@code b <= a}
     * @throws IllegalArgumentException if {@code b - a >= Integer.MAX_VALUE}
     */
    public static int uniform(int a, int b) {
        if ((b <= a) || ((long) b - a >= Integer.MAX_VALUE)) {
            throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
        }
        return a + uniform(b - a);
    }

    /**
     * Returns a random real number uniformly in [a, b).
     *
     * @param a the left endpoint
     * @param b the right endpoint
     * @return a random real number uniformly in [a, b)
     * @throws IllegalArgumentException unless {@code a < b}
     */
    public static double uniform(double a, double b) {
        if (!(a < b)) {
            throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
        }
        return a + uniform() * (b - a);
    }

    /**
     * @param m
     * @param n
     * @param seed seed
     * @return random m-by-n matrix with values between 0 and 1 with seed
     */
    public static double[][] random(int m, int n,int seed) {
        Random rand = new Random(seed);
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
//                a[i][j] = uniform(0.0, 1.0);
                a[i][j] = rand.nextDouble();
            }
        }
        return a;
    }

    /**
     *
     * @param m
     * @param n
     * @return random without seed
     */
    public static double[][] random(int m, int n) {
        Random rand = new Random(seed);
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
//                a[i][j] = uniform(0.0, 1.0);
                a[i][j] = rand.nextDouble();
            }
        }
        return a;
    }

    /**
     * @param n size of one dimensional array
     * @return random one dimension matrix with values between 0 and 1
     */
    public static double[] random1Dim(int n){
        double[] a = new double[n];
        for(int i=0; i<n; i++){
            a[i] = uniform(0.0,1.0);
        }
        return a;
    }

    public static double[][] zero(int h, int w){
        double[][] result = new double[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                result[i][j] = 0;
            }
        }
        return result;
    }

    /**
     * Transpose of a matrix
     *
     * @param a matrix
     * @return b = A^T
     */
    public static double[][] T(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    public static int[][] T(int[][] a){
        int m = a.length;
        int n = a[0].length;
        int[][] b = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    /**
     * @param a matrix
     * @param b matrix
     * @return c = a + b
     */
    public static double[][] add(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        if(b[0].length == 1){
            for(int i =0; i<m; i++){
                for (int j = 0; j < n; j++) {
                    c[i][j] = a[i][j] + b[i][0];
                }
            }
        }else {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    c[i][j] = a[i][j] + b[i][j];
                }
            }
        }
        return c;
    }

    public static double[][] add_Horizontal(double[][] a){
        int m = a.length;
        int n= a[0].length;
        double[][] c = new double[m][1];
        for(int i=0; i< m; i++){
            double sum = 0;
            for(int j =0; j<n; j++){
                sum += a[i][j];
            }
            c[i][0] = sum;
        }
        return c;
    }

    /**
     * @param a matrix
     * @param b matrix
     * @return c = a - b
     */
    public static double[][] subtract(double[][] a, double[][] b) {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }
        }
        return c;
    }


    /**
     * Element wise subtraction
     *
     * @param a scalar
     * @param b matrix
     * @return c = a - b
     */
    public static double[][] subtract(double a, double[][] b) {
        int m = b.length;
        int n = b[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a - b[i][j];
            }
        }
        return c;
    }

    /**
     * simulate how filters are multiplied with conv layer
     * @param a a slice of the conv layer
     * @param b a slice of the filter
     * @return
     */
    public static double dot(double[][] a, double[][] b){
        int height1 = a.length;
        int width1 = a[0].length;
        int height2 = b.length;
        int width2 = b[0].length;
        if (height1!=height2 || width1!=width2) {
            throw new RuntimeException("Illegal matrix dimensions for math.dot");
        }
        double result = 0;
        for(int i=0; i< height1; i++){
            for(int j=0; j<width1;j++){
                double tmp = a[i][j] * b[i][j];
                result += tmp;
            }
        }
        return result;
    }



    /**
     * @param a matrix
     * @param b matrix
     * @return c = a * b
     */
    public static double[][] multiply(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++) {
            for (int j = 0; j < n2; j++) {
                for (int k = 0; k < n1; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }


    /**
     * Element wise multiplication
     *
     * @param a matrix
     * @param x matrix
     * @return y = a * x
     */
    public static double[][] elementWise_multiply(double[][] x, double[][] a) {
        int m = a.length;
        int n = a[0].length;

        if (x.length != m || x[0].length != n) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[][] y = new double[m][n];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                y[j][i] = a[j][i] * x[j][i];
            }
        }
        return y;
    }

    /**
     * Element wise multiplication
     *
     * @param a matrix
     * @param x scaler
     * @return y = a * x
     */
    public static double[][] multiply(double x, double[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] y = new double[m][n];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                y[j][i] = a[j][i] * x;
            }
        }
        return y;
    }

    /**
     * Element wise power
     *
     * @param x matrix
     * @param a scaler
     * @return y
     */
    public static double[][] power(double[][] x, int a) {
        int m = x.length;
        int n = x[0].length;

        double[][] y = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                y[i][j] = Math.pow(x[i][j], a);
            }
        }
        return y;
    }

    /**
     * @param a matrix
     * @return shape of matrix a
     */
    public static String shape(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        String Vshape = "(" + m + "," + n + ")";
        return Vshape;
    }

    /**
     * @param a matrix
     * @return sigmoid of matrix a
     */
    public static double[][] sigmoid(double[][] a) {
        int m = a.length;
        int n = a[0].length;
        double[][] z = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = (1.0 / (1 + Math.exp(-a[i][j])));
            }
        }
        return z;
    }

    public static double[][] relu(double[][] a){
        int m = a.length;
        int n = a[0].length;
        double[][] z = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = Math.max(0,a[i][j]);
            }
        }
        return z;
    }

    /**
     * Element wise division
     *
     * @param a scaler
     * @param x matrix
     * @return x / a
     */
    public static double[][] divide(double[][] x, int a) {
        int m = x.length;
        int n = x[0].length;

        double[][] z = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = (x[i][j] / a);
            }
        }
        return z;
    }
    /**
     * Element wise division
     *
     * @param A matrix
     * @param Y matrix
     * @param batch_size scaler
     * @return loss
     */
    public static double cross_entropy(int batch_size, double[][] Y, double[][] A) {
        int m = A.length;
        int n = A[0].length;
        double[][] z = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = (Y[i][j] * Math.log(A[i][j])) + ((1 - Y[i][j]) * Math.log(1 - A[i][j]));
            }
        }

        double sum = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum += z[i][j];
            }
        }
        return -sum / batch_size;
    }

    public static double[][] softmax(double[][] z) {
        double[][] transposed = T(z);

        int a = transposed.length;
        int b = transposed[0].length;
        double sum = 0;
        double[][] transposedResult = new double[a][];
        for (int i = 0; i < a; i++) {
            double[] temp = new double[b];
            for (int j = 0; j < b; j++) {
                sum += Math.exp(transposed[i][j]);
            }

            for(int x=0; x < b; x++) {
                temp[x] = Math.exp(transposed[i][x]) / sum;
            }
            transposedResult[i] = temp;
            sum=0;
        }

        return T(transposedResult);
    }

    public static double[][] norm(double[][] matrix){

        int h = matrix.length;
        int w = matrix[0].length;
        double[][] result = new double[h][1];
        for (int i = 0; i < h; i++) {
            double RowSum = 0;
            for (int j = 0; j < w; j++) {
                RowSum += Math.pow(matrix[i][j], 2);
            }
            result[i][0] = Math.sqrt(RowSum);
        }
        return result;
    }

    /**
     * Normalize row of matrix by dividing the norm of each row
     * @param matrix input matrix
     * @return normalized matrix with entries between [0,1]
     */
    public static double[][] normalize(double[][] matrix){
        double[][] matrixNorm = norm(matrix);
        int h = matrix.length;
        int w = matrix[0].length;
        double[][] result = new double[h][w];
        for(int i = 0; i < h; i++){
            double rowNorm = matrixNorm[i][0];
            for (int j = 0; j < w; j++) {
                // prevent divide by 0
                if(rowNorm == 0){
                    result[i][j] =0;
                }else{
                    result[i][j] = matrix[i][j] / rowNorm;
                }
            }
        }
        return result;
    }


    /**
     * Function squares each element in the matrix and sum them up
     * @param input matrix
     * @return double
     */
    public static double sum_squared(double[][] input){
        int h = input.length;
        int w = input.length;
        double result = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                result += Math.pow(input[i][j],2);
            }
        }
        return result;
    }


    public static void print(String val) {
        System.out.println(val);
    }

    public static double[][] choseColumn(double[][] a, int start, int end){
        double[][] result_trans = new double[end-start][];
        double[][] a_trans = T(a);
        int count = 0;
        for (int i = start; i < end; i++) {
            result_trans[count] = a_trans[i];
            count++;
        }
        return T(result_trans);
    }

    public static double[] getFirstColumn(double[][] a){
        double[] result = new double[a.length];
        for(int i = 0; i< a.length; i++){
            result[i] = a[i][0];
        }
        return result;
    }

}
