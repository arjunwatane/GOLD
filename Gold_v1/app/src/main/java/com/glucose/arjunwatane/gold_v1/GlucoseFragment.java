package com.glucose.arjunwatane.gold_v1;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Math.pow;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GlucoseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlucoseFragment extends Fragment
{
    private Context mainContext = getActivity();
    DatabaseHelper helper;
    Button b_sample, b_train, b_polyfit, b_PCA, b_test, b_testpolyfit, b_testpca;
    EditText inputGlucose;
    TextView outputGlucose;
    String stringGlucose;
    //public double glucose_database[][] = new double[50][100];
    public double data_filtered[][] = new double[50][100];
    PrincipleComponentAnalysis data_PCA = new PrincipleComponentAnalysis();
    public double data_polyfit_coef[];
    public int size = 0;
    public double test_filtered[], coefficients[], train_actual_values[] = new double[50];
    public double train_results[][];
    public double glucose_database[][] = new double[10][197];
    public int numComponents = 5;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private float mParam3 [] ;


    public GlucoseFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GlucoseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GlucoseFragment newInstance(String param1, String param2, float spectrum_data[])
    {
        GlucoseFragment fragment = new GlucoseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putFloatArray(ARG_PARAM3, spectrum_data);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getFloatArray(ARG_PARAM3);
        }

        //set up database connection
        helper = new DatabaseHelper(mainContext);
        helper.load();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mainContext = context;
    }

    //Arjun start: click glucose button to start pre-processing
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_glucose, container, false);
        b_sample = (Button) rootView.findViewById(R.id.button_sample_glucose);
        b_train = (Button) rootView.findViewById(R.id.button_filter_glucose);
        b_polyfit = (Button) rootView.findViewById(R.id.button_polyfit_glucose);
        b_PCA = (Button) rootView.findViewById(R.id.button_PCA_glucose);
        b_test = (Button) rootView.findViewById(R.id.button_test_glucose);
        b_testpolyfit = (Button) rootView.findViewById(R.id.button_testpolyfit_glucose);
        b_testpca = (Button) rootView.findViewById(R.id.button_testpca_glucose);
        inputGlucose = (EditText) rootView.findViewById(R.id.inputGlucose);


        b_sample.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sample(inputGlucose.getText().toString());
            }
        });

        b_train.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                train_setup();
            }
        });

        b_polyfit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                train_polyfit();
            }
        });

        b_PCA.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                train_PCA();
            }
        });

        b_test.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                test_data();
            }
        });

        b_testpolyfit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                test_polyfit();
            }
        });

        b_testpca.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                test_PCA();
            }
        });


        return rootView;
    }

    /** Get one sample data from the hardware module, and add it to the database **/
    public void sample(String known_glucose_string){

        double known_glucose = Double.parseDouble(known_glucose_string);
        //float data[] = BluetoothFragment.glucose_array;
        float data[] = mParam3;
        double spec_double[] = new double[data.length];
        for(int i=0; i<spec_double.length; i++) {
            spec_double[i] = (double)data[i];
        }
        train_actual_values[size] = known_glucose;///////////////////////////////////////////////////////////////////////
        helper.saveTVals(train_actual_values);
        glucose_database[size] = spec_double;/////////////////////////////////////////////////////////////////////
        size++;
        System.out.println(size);
        //return glucose_database;
    }

    /** TRAIN DATA and return derived + smoothed database **/
    public void train_setup() {

        GlucoseFilter train = new GlucoseFilter();
        train_results = glucose_database;

        //1st derivative
        train_results = train.firstDerivative(train_results);

        //smooth data
        train_results = train.smooth(train_results);//////////////////////////////////////////////////////////////////////////////////////////////////////////

        //return train_results;
        System.out.println(train_results.length + " x " + train_results[0].length);

    }

    /** Create polynomial fitting of training data **/
    public void train_polyfit(){
        GlucoseFilter train = new GlucoseFilter();

        coefficients = train.polynomialFit(train_results, train_actual_values);
        //return coefficients;
    }

    /** Principle Component Analysis of training data **/
    public void train_PCA(){
        GlucoseFilter train = new GlucoseFilter();
        data_PCA = new PrincipleComponentAnalysis();

        data_PCA.setup(train_results.length, train_results[0].length);
        for(int i = 0; i < train_results.length; i ++){
            data_PCA.addSample(train_results[i]);
        }

        data_PCA.computeBasis(numComponents);
    }

    /** Test data against Training Data **/
    public void test_data(){

        float data[] = BluetoothFragment.glucose_array;

        double gluc_double[] = new double[data.length];
        for(int i=0; i<gluc_double.length; i++) {
            gluc_double[i] = (double)data[i];
        }

        GlucoseFilter test = new GlucoseFilter();

        test_filtered = test.firstDerivative(gluc_double);
        test_filtered = test.smooth(test_filtered);

    }

    /** Test data by applying poly fit function **/
    public void test_polyfit(){
        double sum = 0, average, result=0, exp;

        for(int i=0; i < test_filtered.length; i++) {
            sum += test_filtered[i];
        }
        average = sum / test_filtered.length;
        exp = coefficients.length;

        for(int i = 0; i < coefficients.length; i ++){
            exp --;
            result += coefficients[i] * pow(average,exp);
        }
    }

    public void test_PCA(){
        //data_PCA.errorMembership(test_filtered);
        //data_PCA.sampleToEigenSpace(test_filtered);
        data_PCA.response(test_filtered);
    }




    public void filter(){
    }
    //Arjun end:
}
