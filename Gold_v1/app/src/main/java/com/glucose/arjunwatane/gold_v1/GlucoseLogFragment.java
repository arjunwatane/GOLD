package com.glucose.arjunwatane.gold_v1;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GlucoseLogFragment extends Fragment {

    DatabaseHelper helper;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mainContext = getActivity();
    private String mParam1;
    private int mParam2;

    public static GlucoseLogFragment newInstance(String param1, int param2)
    {
        GlucoseLogFragment fragment = new GlucoseLogFragment();
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
        ArrayList<DatabaseHelper.QuerySet> tableSet;
        View rootView = inflater.inflate(R.layout.fragment_glucose_log, container, false);
        tableSet = helper.search(mParam1, mParam2);


        if(tableSet.size() != 0)
        {
            for (DatabaseHelper.QuerySet query : tableSet)
            {
                final TableLayout tl_logTable = (TableLayout) rootView.findViewById(R.id.tl_logTable);
                final TableRow tableRow = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.tablerow, null);
                TextView tv;

                tv = (TextView) tableRow.findViewById(R.id.tableCell1);
                tv.setText(query.glucoseReading);


                tv = (TextView) tableRow.findViewById(R.id.tableCell2);
                tv.setText(query.timestamp);

                tl_logTable.addView(tableRow);
            }
        }
        else
        {
            Toast.makeText(getActivity(), "There aren't any logs to display.", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }


}
