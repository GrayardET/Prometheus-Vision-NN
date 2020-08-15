package Experiment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ReferenceTest {
    public static void main(String args[]){
        int[] a = {1,2,3};
        int[] b = {7,8,9};
        a = Arrays.copyOf(a,3);
        a[0]=5;
        System.out.println(Arrays.toString(b));

        int[][] c = {{1}, {2}};
        int[][] d = {{3}, {4}};
        int[][][] e = {{{5}, {6}}};
        int[][][] e_copy = Arrays.copyOf(e,1);
        System.out.println(Arrays.deepToString(e_copy));
        ArrayList<int[][]> intList = new ArrayList<>();
        intList.add(c);
        c[0][0] = 0;
        c=d;
        System.out.println(Arrays.deepToString(intList.get(0)));

        HashMap<String, Double> hashMap = new HashMap<>();
        hashMap.put("cat",1.0);
        hashMap.put("dog", 2.0);
        System.out.println(hashMap.get("cat"));
    }
}
