package com.hfad.conf_me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class EditProfileFragment extends Fragment {

    TextView editName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_edit_profile, null);
        editName = (TextView) view.findViewById(R.id.edit_name);
        Bundle bundle = getArguments();

        return view;
    }
}


