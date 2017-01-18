package com.glucose.arjunwatane.gold_v1;

public class MovingAverage {
    double[] average;
    //5 item moving window
    static double denominator = 5.0;


    /*
    MovingAverage(double[][] average){
        for(int i=0; i<average.length; i++){
            for(int j=0; j<average.length-denominator+1; j++){
                int sum=0;
                for(int k=0; k<denominator; k++) sum += average[i][k+j];
                average[i][j] = movavg(sum);
            }
        }
        avg = average;
    } */

    MovingAverage(double[] spectrum){
       // average = new double[spectrum.length-(((int)denominator-1)*2)];
        average = new double[spectrum.length-(int)denominator+1];

        for(int i = 0; i < average.length; i ++)
        {
            double sum = 0;

            for(int k = i; k < denominator+i; k ++)
            {
                sum += spectrum[k];
            }

            average[i] = sum/denominator;

        }

    }

    //calculate average
    double movavg(double sum)
    {
        return (sum/denominator);
    }

    //return averaged matrix
    double[] getAvg(){


        return average;
    }
}
