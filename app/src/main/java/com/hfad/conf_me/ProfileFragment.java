package com.hfad.conf_me;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.conf_me.models.User;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView userName;
    private final int REQUEST_PREMISSION_CAMERA = 1;
    RelativeLayout mainLayout;

    String currentUid;
    private Camera camera;
    private LinearLayout editProfile;

    FragmentManager myFragmentManager;
    EditProfileFragment editProfileFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View rootView =
                inflater.inflate(R.layout.fragment_profile, container, false);




        LinearLayout editProfile = (LinearLayout) rootView.findViewById(R.id.edit_profile);

        Button btn_tag1 = (Button) rootView.findViewById(R.id.tag1);
        Button btn_tag2 = (Button) rootView.findViewById(R.id.tag2);
        Button btn_tag3 = (Button) rootView.findViewById(R.id.tag3);


        TextView userName = (TextView) rootView.findViewById(R.id.user_name);
        TextView nickname = (TextView) rootView.findViewById(R.id.nickname);
        ImageView user_foto = (ImageView) rootView.findViewById(R.id.user_foto);
        TextView user_email = (TextView) rootView.findViewById(R.id.text_email);
        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUid = userId.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("Users/");
        myRef.child(currentUid).child("user_id").setValue(currentUid);
        myRef.child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName()+" "+ user.getSurname());
                nickname.setText("@"+user.getNickname());
                user_email.setText(user.getEmail());
                btn_tag1.setText(user.getTag1());
                btn_tag2.setText(user.getTag2());
                btn_tag3.setText(user.getTag3());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://confme-fa7aa.appspot.com");


        storageRef.child("avatars/"+currentUid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.get().load(uri).into(user_foto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });








        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditProfileFragment editProfileFragment = new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editProfileFragment).addToBackStack(null).commit();
            }

        });


        return rootView;

    }


    @Override
    public void onClick(View v) {

    }

}



