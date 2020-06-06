package com.hfad.conf_me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.conf_me.models.User;



public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView userName;
    private TextView text_city;


    String currentUid;
    private LinearLayout editProfile;

    FragmentManager myFragmentManager;
    EditProfileFragment editProfileFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){


        View rootView =
                inflater.inflate(R.layout.fragment_profile, container, false);



        Button arbutton = (Button) rootView.findViewById(R.id.arbutton);
        Button sign_out = (Button) rootView.findViewById(R.id.sign_out);
        LinearLayout editProfile = (LinearLayout) rootView.findViewById(R.id.edit_profile);

        Button button = (Button) rootView.findViewById(R.id.arbutton);
        TextView userName = (TextView) rootView.findViewById(R.id.user_name);
        TextView text_city = (TextView) rootView.findViewById(R.id.text_city);
        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUid = userId.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("Users/");

        myRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName()+" "+ user.getSurname());
                text_city.setText(user.getCity());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        button.setOnClickListener(this);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });







        return rootView;

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getActivity(), "Вы нажали на кнопку",
                Toast.LENGTH_SHORT).show();
    }
}
