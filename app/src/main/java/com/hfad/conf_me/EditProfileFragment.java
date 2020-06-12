package com.hfad.conf_me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private EditText edit_name;
    private EditText edit_surname;
    private EditText edit_email;
    private EditText edit_phone;
    private EditText edit_description;
    private TextView cancel;
    private TextView submit;


    String currentUid;
    FragmentManager myFragmentManager;
    Fragment profileFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_edit_profile, container, false);

        TextView cancel = (TextView) rootView.findViewById(R.id.cancel);
        TextView submit = (TextView) rootView.findViewById(R.id.submit);
        EditText edit_name = (EditText) rootView.findViewById(R.id.edit_name);
        EditText edit_surname = (EditText) rootView.findViewById(R.id.edit_surname);
        EditText edit_email = (EditText) rootView.findViewById(R.id.edit_email);
        EditText edit_phone = (EditText) rootView.findViewById(R.id.edit_phone);
        EditText edit_description = (EditText) rootView.findViewById(R.id.edit_description);

        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUid = userId.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("Users/"+ currentUid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment profileFragment = new Fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_name.getText().toString()!= ""){
                String name = edit_name.getText().toString();
                myRef.child("name").setValue(name);
                }
                if (edit_surname.getText().toString()!= ""){
                String surname = edit_surname.getText().toString();
                myRef.child("surname").setValue(surname);
                }
                if (edit_email.getText().toString()!= ""){
                String email = edit_email.getText().toString();
                myRef.child("email").setValue(email);
                }
                if (edit_phone.getText().toString()!= ""){
                    String phone = edit_phone.getText().toString();
                    myRef.child("phone").setValue(phone);
                }
                if (edit_description.getText().toString()!= ""){
                String description = edit_description.getText().toString();
                    myRef.child("description").setValue(description);
                }
                Fragment profileFragment = new Fragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).addToBackStack(null).commit();

            }
        });

        return rootView;

    }

    @Override
    public void onClick(View view) {

    }
}






