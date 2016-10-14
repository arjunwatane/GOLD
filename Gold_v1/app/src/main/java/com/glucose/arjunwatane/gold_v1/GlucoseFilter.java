package com.glucose.arjunwatane.gold_v1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

import static android.R.attr.data;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Arjun on 10/8/16.
 */

public class GlucoseFilter
{

//test Justyn
    
    public int filterMain(int spectrum)
    {

        double coef[] = new double[]{1,-2,3};

        double x[] = new double[]{-2,1,0.5,2,3,4,5,7,8,9.2,10.2,4.3,6.7};
        double y[] = new double[ x.length ];

        for( int i = 0; i < y.length; i++ ) {
            double v = 0;
            double xx = 1;
            for (double c : coef) {
                v += c * xx;
                xx *= x[i];
            }

            y[i] = v;
        }

        PolynomialFit alg = new PolynomialFit(4);

        alg.fit(x,y);

        double found[] = alg.getCoef();

        for( int i = 0; i < coef.length; i++ ) assertEquals(coef[i], found[i], 1e-8);

        Log.d("GlucoseFilter:", Arrays.toString(coef) + Arrays.toString(found));
        return spectrum;


    }

}
