package com.fusion.asterspinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.fusion.library.AsterSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.aster_spinner) AsterSpinner asterSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<String> names = new ArrayList<>();
        names.add("Peter Pan");
        names.add("Superman");
        names.add("Batman");
        names.add("Peter");
        names.add("Bob");
        names.add("Harry");
        names.add("Trex");
        names.add("Ron");
        names.add("Naomi");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_slide_up_selector, names );
        asterSpinner.setAdapter(adapter);

    }
}
