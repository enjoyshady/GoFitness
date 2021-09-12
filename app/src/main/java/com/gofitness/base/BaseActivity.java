package com.gofitness.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gofitness.R;


public abstract class BaseActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment container = fm.findFragmentById(R.id.fragment_container);
        if (container == null) {
            container = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, container)
                    .commit();
        }
    }

}