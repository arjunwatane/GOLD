package com.glucose.arjunwatane.gold_v1;

public class MovingAverage {
    int[][] avg;
    //5 item moving window
    static int denominator = 5;

    MovingAverage(int[][] average){
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
    int movavg(int sum){return (sum/denominator);
    }

    //return averaged matrix
    int[][] getAvg(){
        return avg;
    }
}
296709 297804 298901 300000