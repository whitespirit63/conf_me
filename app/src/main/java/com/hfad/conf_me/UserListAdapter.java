package com.hfad.conf_me;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.conf_me.models.User;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {
    private LayoutInflater inflater;
    private int layout;
    private List<User> users;

    public UserListAdapter(Context context, int resource, List<User> users){
        super(context, resource, users);
        this.users = users;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View view = inflater.inflate(R.layout.list_of_members_item, parent, false);
        ImageView avatar = (ImageView) view.findViewById(R.id.user_avatar_list);
        TextView usernameView = (TextView) view.findViewById(R.id.user_name_list);
        TextView tagsView = (TextView) view.findViewById(R.id.tags_list);
        User user = users.get(position);
        avatar.setImageResource(R.drawable.avatar);
        usernameView.setText(user.getName() + " " + user.getSurname());
        tagsView.setText(user.getEmail());

        return view;
    }
}
