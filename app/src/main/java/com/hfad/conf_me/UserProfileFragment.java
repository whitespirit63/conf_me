package com.hfad.conf_me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

public class UserProfileFragment extends Fragment {

    TextView userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        userName = (TextView) view.findViewById(R.id.user_name);
        Bundle bundle = getArguments();
        userName.setText(bundle.getString("name")+ " " + bundle.getString("surname"));

        return view;
    }
}
