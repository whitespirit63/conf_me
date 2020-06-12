package com.hfad.conf_me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.Nullable;

public class UserProfileFragment extends Fragment {

    TextView userName;
    ImageView btn_open_dialog;
    DialogFragment dialogFragment;
    FragmentManager myFragmentManager;
    String user_name;
    String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        userName = (TextView) view.findViewById(R.id.user_name);
        btn_open_dialog = (ImageView) view.findViewById(R.id.btn_open_dialog);
        Bundle bundle = getArguments();
        userName.setText(bundle.getString("name")+ " " + bundle.getString("surname"));
        user_name = bundle.getString("name");
        user_id = bundle.getString("user_id");

        btn_open_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", user_name);
                bundle.putString("user_id", user_id);
                dialogFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dialogFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
