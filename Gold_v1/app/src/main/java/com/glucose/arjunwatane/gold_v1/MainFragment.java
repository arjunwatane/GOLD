package com.glucose.arjunwatane.gold_v1;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private int mParam2;
    private Context mainContext = getActivity();
    Button btnTest;

    DatabaseHelper helper;


    public MainFragment()
    {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, int param2)
    {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainContext = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        helper = new DatabaseHelper(mainContext);
        helper.load();

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //helper.load();
        btnTest = (Button) rootView.findViewById(R.id.btnTest);

        btnTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int randomNum = 0;
                Random ran = new Random();
                randomNum  = ran.nextInt(50) + 70;
                helper.insertLog(mParam1, mParam2, randomNum);
            }
        });

        return rootView;
    }

}
