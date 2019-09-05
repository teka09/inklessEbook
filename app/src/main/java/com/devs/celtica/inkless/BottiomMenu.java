package com.devs.celtica.inkless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.devs.celtica.inkless.Activities.Accueil;
import com.devs.celtica.inkless.Publications.SearchBook;
import com.devs.celtica.inkless.Users.Profile;


public class BottiomMenu extends Fragment {

    AppCompatActivity c;
    public BottiomMenu() {
        // Required empty public constructor
    }

    public static BottiomMenu newInstance() {
        BottiomMenu fragment = new BottiomMenu();
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
        View v= inflater.inflate(R.layout.fragment_bottiom_menu, container, false);
        final LinearLayout profile=v.findViewById(R.id.bottom_menu_profile);
        LinearLayout stat=v.findViewById(R.id.bottom_menu_stat);
        LinearLayout search=v.findViewById(R.id.bottom_menu_search);
        LinearLayout saving=v.findViewById(R.id.bottom_menu_saving);
        LinearLayout accueil=v.findViewById(R.id.bottom_menu_accueil);

        accueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(c, Accueil.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                c.startActivity(i);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(c, Profile.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                c.startActivity(i);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(c, SearchBook.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                c.startActivity(i);
            }
        });
        return v;
    }


}
