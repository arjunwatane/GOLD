package com.glucose.arjunwatane.gold_v1;

public class BeerLambert {
    static final int rows=100, cols=500;
    //trans spectrum of component
    int tcomp;
    //trans spectrum of reference sample (component free) constant
    int tref;
    //concentration
    int c;
    //path length
    int l;

    //matrix to store input data
    private int[][] data = new int[rows][cols];
    int wave;

    BeerLambert(int wavelength){
        wave = wavelength;

        compute(int[][] input);
    }

    public void compute(int[][] input){
        //accept input into function


    }

    public int[][] getData() {
        return data;
    }
}
