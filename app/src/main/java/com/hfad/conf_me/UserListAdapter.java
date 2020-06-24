package com.hfad.conf_me;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hfad.conf_me.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends ArrayAdapter<User> {
    private Context context;
    private LayoutInflater inflater;
    private List<User> users;

    public UserListAdapter(Context context, List<User> users){
        super(context, R.layout.list_of_members_item, users);
        this.users = users;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(R.layout.list_of_members_item, parent, false);
        CircleImageView avatar = (CircleImageView) view.findViewById(R.id.user_avatar_list);
        TextView usernameView = (TextView) view.findViewById(R.id.user_name_list);
        TextView tagsView = (TextView) view.findViewById(R.id.tags_list);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://confme-fa7aa.appspot.com");
        User user = users.get(position);

        usernameView.setText(user.getName() + " " + user.getSurname());
        tagsView.setText(user.getTag1());

        storageRef.child("avatars/"+user.getUser_id()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Picasso.get().load(uri).into(avatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        return view;
    }
}
