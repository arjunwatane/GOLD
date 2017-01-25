package com.glucose.arjunwatane.gold_v1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

import static android.R.attr.data;
import static junit.framework.Assert.assertEquals;

/** Need to change everything to 2D arrays for training **/
public class GlucoseFilter
{

    //public void getGlucoseData(){}
    //public void getOxygenData(){}
    //public void getSkinData(){}

    /** Function to convert float array to double array **/
    public double[] convertData(float glucose_data[]){
        double spec_double[] = new double[glucose_data.length];
        for(int i=0; i<spec_double.length; i++) {
            spec_double[i] = (double)glucose_data[i];
        }
        return spec_double;
    }

    /** Function to calculate first derivative of the spectrum **/
    public double[] firstDerivative(double glucose_data[]){
        Deriv derivation = new Deriv();
        double[] result =  derivation.firstOrderDerivative(1.0, glucose_data, 2);
        //Log.d("Derived Result: ", Arrays.toString(result));
        return result;
    }

    public double[][] firstDerivative(double glucose_data[][]){
        double results[][] = new double[glucose_data.length][glucose_data[0].length];
        for(int i = 0; i < glucose_data.length; i ++) {
            results[i] = firstDerivative(glucose_data[i]);
        }
        return results;
    }




    /** Function to smooth the spectrum by using a moving window **/
    public double[] smooth(double glucose_data[]){
        MovingAverage av = new MovingAverage(glucose_data);
        double [] result_averaged = av.getAvg();
        //Log.d("Averaged Result: ", Arrays.toString(result_averaged));
        return result_averaged;
    }

    public double[][] smooth(double glucose_data[][]){
        double results[][] = new double[glucose_data.length][glucose_data[0].length];
        for(int i = 0; i  < glucose_data.length; i ++){
            results[i] = smooth(glucose_data[i]);
        }
        return results;
    }

    /** Function to calculate PCA. Need to change to 2D array of training data **/
    public void princCompAnalysis(double glucose_data[][], double glucose_input[]){

        //double training_set[][] = new double [glucose_data.length][glucose_data[0].length];
        double training_set[][] = glucose_data;
        PrincipleComponentAnalysis alg2 = new PrincipleComponentAnalysis();
        alg2.setup(training_set.length, glucose_input.length);   // alg2.setup(#samples, #data-points per sample)
        for(int i = 0; i<training_set.length; i ++)
            alg2.addSample(training_set[i]);    //Add sample sto the PCA object
        alg2.computeBasis(5);   //  Compute PCA alg2.computeBasis(#number of components)


        //TESTING
        double results[];
        results = alg2.sampleToEigenSpace(glucose_input);
        results = alg2.eigenToSampleSpace(glucose_input);
        double error = alg2.errorMembership(glucose_input);
        double response = alg2.response(glucose_input);
    }

        public PrincipleComponentAnalysis PCA(double glucose_data[][], int num){

            PrincipleComponentAnalysis train_obj = new PrincipleComponentAnalysis();
            double results[][] = new double [glucose_data.length][glucose_data[0].length];

            train_obj.setup(glucose_data.length, glucose_data[0].length);
            for(int i = 0; i < glucose_data.length; i ++){
                train_obj.addSample(glucose_data[i]);
            }

            train_obj.computeBasis(num);
            return train_obj;

        }

    /** Function to calculate polynomial fitting coefficients **/
    public double[] polyFit(double glucose_data[][], double glucose_input[]){

        double x_index[] = new double[glucose_data.length];
        for(int i=0; i < glucose_data.length; i++)
            x_index[i] = (double)i;

        PolynomialFit polyfit = new PolynomialFit(3);
        polyfit.fit(x_index,glucose_input);
        polyfit.removeWorstFit();

        double spec_coefficients[] = polyfit.getCoef();
        //Log.d("GlucoseFilter:", Arrays.toString(spec_coefficients));

        return spec_coefficients;
    }

    public double [] polynomialFit(double glucose_data[][], double gluc_value_arr[]){

        PolynomialFit polyfit = new PolynomialFit(3);
        int sum = 0;
        double average;
        double[] results= new double[glucose_data.length];
        for(int i=0; i < glucose_data.length; i++)
        {
            sum = 0;

            for(int j = 0; j < glucose_data[i].length; j ++)
            {
                sum += glucose_data[i][j];
            }
            average = sum / glucose_data[i].length;
            results[i] = average;
        }

        /*
        double x_index[] = new double[results.length];
        for(int i=0; i < results.length; i++)
            x_index[i] = (double)i; */

        polyfit.fit(gluc_value_arr, results);
        polyfit.removeWorstFit();
        double coefficients[] = polyfit.getCoef();

        return coefficients;

    }


    public void beerLambert(){}



    
}
