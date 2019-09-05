package com.devs.celtica.inkless;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class HeaderFrag extends Fragment {

    AppCompatActivity c;
    public HeaderFrag() {
        // Required empty public constructor
    }

    public static HeaderFrag newInstance() {
        HeaderFrag fragment = new HeaderFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        c= (AppCompatActivity) getActivity();
        View v= inflater.inflate(R.layout.fragment_header, container, false);
        ImageView retour=(ImageView) v.findViewById(R.id.retour);


        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.finish();
            }
        });


        return v;
    }


}
