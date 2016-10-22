package com.glucose.arjunwatane.gold_v1;

public class BeerLambert {
    static final int rows=100, cols=500;
    //trans spectrum of component
    double tcomp;
    //trans spectrum of reference sample (component free) constant
    double tref;
    //concentration
    double c;
    //path length
    double l;

    //matrix to store input data
    private double[][] data = new double[rows][cols];
    int wave;

    BeerLambert(int wavelength, double con, double len){
        wave = wavelength;
        c = con;
        l = len;
        compute(double[][] input);
    }

    //must accept input matrix
    //calibration beerlambert
    public void compute(double[][] input){
        //set tcomp--glucose

        //work down the col
        for(int i=0; i<rows; i++){
            //work along the row
            for(int j=0; j<cols; j++){
                //absorbance of sample = -log_10(tcomp/tref) / (length * concentration)
                input[i][j] = (-1.0) * Math.log10(tcomp/tref) / (l*c);
            }
        }

        //return the computed absorbance
        data = input;
    }

    public double[][] getData() {
        return data;
    }
}
