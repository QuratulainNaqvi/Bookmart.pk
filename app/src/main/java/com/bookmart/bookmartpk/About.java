package com.bookmart.bookmartpk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class About extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_activity, container1, false);
    }

    @Override
    public void onViewCreated(final View view1, Bundle savedInstanceState) {

        getActivity().setTitle("About Us");
    }

}
