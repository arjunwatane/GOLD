package com.glucose.arjunwatane.gold_v1;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GlucoseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlucoseFragment extends Fragment
{
    Button b;
    EditText inputGlucose;
    TextView outputGlucose;
    String stringGlucose;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    public static GlucoseFragment newInstance(String param1, String param2)
    {
        GlucoseFragment fragment = new GlucoseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_glucose, container, false);
        b = (Button) rootView.findViewById(R.id.button_glucose);
        inputGlucose = (EditText) rootView.findViewById(R.id.inputGlucose);
        outputGlucose = (TextView) rootView.findViewById(R.id.outputGlucose);


        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stringGlucose = inputGlucose.getText().toString();
                outputGlucose.setText(stringGlucose);
            }
        };
        b.setOnClickListener(listener);
        return rootView;
    }

}
