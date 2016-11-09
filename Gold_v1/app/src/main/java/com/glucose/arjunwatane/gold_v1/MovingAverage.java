package com.glucose.arjunwatane.gold_v1;

public class MovingAverage {
    double[][] avg;
    //5 item moving window
    static double denominator = 5.0;

    MovingAverage(double[][] average){
        for(int i=0; i<average.length; i++){
            for(int j=0; j<average.length-denominator+1; j++){
                int sum=0;
                for(int k=0; k<denominator; k++) sum += average[i][k+j];
                average[i][j] = movavg(sum);
            }
        }
        avg = average;
    }

    //calculate average
    double movavg(double sum){return (sum/denominator);
    }

    //return averaged matrix
    double[][] getAvg(){
        return avg;
    }
}
