package com.glucose.arjunwatane.gold_v1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

import static android.R.attr.data;
import static junit.framework.Assert.assertEquals;

public class GlucoseFilter
{



    public void filterPolyFit(float spectrum[])

    {
        /*TODO:
            1) Receive data of glucose
            2) Receive data of skin
            3) Receive data of pulse oximetry
                3A) Calculate heart rate from this (usingk peaks/time)
        */

        //Data setup
        double x_index[] = new double[200];
        double spec_double[] = new double[200];
        for(int i=0; i<spec_double.length; i++) {
            x_index[i] = (double)i;
            spec_double[i] = (double)spectrum[i];
        }


        //Polynomial Fit
        PolynomialFit polyfit = new PolynomialFit(3);
        polyfit.fit(x_index,spec_double); //TODO: update poly fit method
        polyfit.removeWorstFit();
        double spec_coefficients[] = polyfit.getCoef();
        Log.d("GlucoseFilter:", Arrays.toString(spec_coefficients));


        //PCA
        PrincipleComponentAnalysis alg2 = new PrincipleComponentAnalysis();
        alg2.setup(100, 200);   // alg2.setup(#samples, #data-points per sample)
        for(int i = 1; i<=100; i ++)
            alg2.addSample(spec_double);    //Add sample sto the PCA object
        System.out.println("added samples");
        alg2.computeBasis(5);   //  Compute PCA alg2.computeBasis(#number of components)
/*
        //generic derivative
        Derive derived_data = new Derive(spec_double);

        //derived
       // double[][] result = derive.getData();
       // double[] derived_result = derived_data.getData();

       Deriv derivation = new Deriv();
       double[] result =  derivation.firstOrderDerivative(1.0, spec_double, 2);

        Log.d("Derived Result: ", Arrays.toString(result));
        //calculate moving average of derived data
        MovingAverage av = new MovingAverage(result);

        //derived and averaged
        double [] result_averaged = av.getAvg();
        Log.d("Averaged Result: ", Arrays.toString(result_averaged));
        Log.d("Length of ave arr: ", Integer.toString(result_averaged.length));

*/


        //for( int i = 0; i < coef.length; i++ )
        //    assertEquals(coef[i], found[i], 1e-8);



        //return spectrum;
    }
}
