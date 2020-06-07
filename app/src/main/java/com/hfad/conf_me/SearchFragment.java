package com.hfad.conf_me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.conf_me.models.User;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {


    private List<User> users = new ArrayList();
    ListView listViewUsers;
    FirebaseDatabase db;
    DatabaseReference usersDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        db = FirebaseDatabase.getInstance();
        usersDB = db.getReference().child("Users/");
        //Загрузить начальный список
        setInitialData();
        listViewUsers = (ListView) view.findViewById(R.id.list_of_users);
        UserListAdapter adapter = new UserListAdapter(getActivity(), R.layout.list_of_members_item, users);
        listViewUsers.setAdapter(adapter);
        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем выбранный пункт
                User selectedState = (User)parent.getItemAtPosition(position);
                //TODO: Перейти в профиль человек
            }
        };
        listViewUsers.setOnItemClickListener(itemListener);
        return view;
    }

    private void setInitialData(){
        usersDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
