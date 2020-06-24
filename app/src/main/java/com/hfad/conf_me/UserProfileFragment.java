package com.hfad.conf_me;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

public class UserProfileFragment extends Fragment {

    TextView userName;
    Button btn_open_dialog;
    DialogFragment dialogFragment;
    FragmentManager myFragmentManager;
    String user_name;
    String user_id;
    String user_tag1;
    String user_tag2;
    String user_tag3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        userName = (TextView) view.findViewById(R.id.user_name);
        Button btn_tag1 = (Button) view.findViewById(R.id.tag1);
        Button btn_tag2 = (Button) view.findViewById(R.id.tag2);
        Button btn_tag3 = (Button) view.findViewById(R.id.tag3);
        btn_open_dialog = (Button) view.findViewById(R.id.btn_open_dialog);
        ImageView user_foto = (ImageView) view.findViewById(R.id.user_foto);
        TextView nickname = (TextView) view.findViewById(R.id.title);
        TextView user_email = (TextView) view.findViewById(R.id.text_email);
        Bundle bundle = getArguments();
        userName.setText(bundle.getString("name")+ " " + bundle.getString("surname"));
        nickname.setText("@"+bundle.getString("nickname"));
        btn_tag1.setText(bundle.getString("tag1"));
        btn_tag2.setText(bundle.getString("tag2"));
        btn_tag3.setText(bundle.getString("tag3"));
        user_email.setText(bundle.getString("email"));
        user_name = bundle.getString("name");
        user_id = bundle.getString("user_id");


        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://confme-fa7aa.appspot.com");


        storageRef.child("avatars/"+user_id+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.get().load(uri).into(user_foto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });




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
