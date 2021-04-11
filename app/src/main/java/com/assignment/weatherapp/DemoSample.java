package com.assignment.weatherapp;

import java.util.ArrayList;
import java.util.List;

public class DemoSample {

    public static void main(String []args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(12);
        numbers.add(5);
        numbers.add(14);
        numbers.add(44);
        numbers.add(40);
        int res = getLeastDifference(numbers);
        System.out.println("RESULT = " + res);
    }

    public static int getLeastDifference(List<Integer> numbers ) {
        if (numbers.size() <= 1) {
            return 0;
        } else {
            int res = numbers.get(0) - numbers.get(1);
            if (res < 0) res = -res;
            int diff;
            for (int i = 0; i < numbers.size() - 1; i++) {
                diff = numbers.get(i) - numbers.get(i + 1);
                if (diff < 0) {
                    diff = -diff;
                }
                if (res < diff) {
                    res = diff;
                }
            }
            return res;
        }
    }
    
}






