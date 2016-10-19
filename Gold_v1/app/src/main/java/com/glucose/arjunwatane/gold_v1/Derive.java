package com.glucose.arjunwatane.gold_v1;

public class Derive {
    static final int rows=100, cols=500;
    int[][] data;

    //test constructor
    Derive(){
        //example raw wavelength
        int[][] wave1 = new int[rows][cols];

        for(int i=0; i<wave1.length; i++){
            //populate
            for(int j=0; j<wave1[0].length; j++){
                wave1[i][j] = j+i+2;//multiply an index j by its exponent
                System.out.print(wave1[i][j] +" ");
            }
            System.out.println();
        }

        wave1 = derivative(wave1);
        data = wave1;
    }

    //process the spectrum with first derivative
    Derive(int[][] spectrum){
        spectrum = derivative(spectrum);
        data = spectrum;
    }

    //take first derivative
    int[][] derivative(int[][] spectrum){
        for(int i=0; i<spectrum.length; i++){
            for(int j=0; j<spectrum[0].length; j++){
                spectrum[i][j] *= j+1;
            }
        }
        return spectrum;//return derived spectrum
    }

    //return derived matrix
    public int[][] getData(){
        return data;
    }
}
