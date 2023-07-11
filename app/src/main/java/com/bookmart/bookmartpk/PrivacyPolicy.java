package com.bookmart.bookmartpk;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PrivacyPolicy extends Fragment {
    private TextView link1,link2,link3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_privacy_policy, container1, false);
    }

    @Override
    public void onViewCreated(final View view1, Bundle savedInstanceState) {
        getActivity().setTitle("Privacy Policy");

        link1 = view1.findViewById(R.id.hyperlink1);
        link2 = view1.findViewById(R.id.hyperlink2);
        link3 = view1.findViewById(R.id.hyperlink3);

        link1.setMovementMethod(LinkMovementMethod.getInstance());
        link2.setMovementMethod(LinkMovementMethod.getInstance());
        link3.setMovementMethod(LinkMovementMethod.getInstance());
    }
}