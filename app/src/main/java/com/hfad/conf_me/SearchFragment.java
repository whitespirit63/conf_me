package com.hfad.conf_me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SearchFragment extends Fragment {

    private List<User> users = new ArrayList();
    private List<User> searchUser = new ArrayList();
    private ListView listViewUsers;
    private DatabaseReference dataBase;
    private DatabaseReference usersDB;
    private FirebaseUser currentUser;
    public boolean[] array = new boolean[6];
    UserListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        dataBase = FirebaseDatabase.getInstance().getReference("Users/");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        EditText search_edit = (EditText) view.findViewById(R.id.search_user_text);




        Button backend = (Button) view.findViewById(R.id.backend);
        Button frontend = (Button) view.findViewById(R.id.frontend);
        Button design = (Button) view.findViewById(R.id.design);
        Button devops = (Button) view.findViewById(R.id.devops);
        Button data_science = (Button) view.findViewById(R.id.data_science);
        Button python = (Button) view.findViewById(R.id.python);

        backend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor(backend, 0);
            }
        });
        frontend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor(frontend, 1);
            }
        });
        design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor(design, 2);
            }
        });
        devops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changecolor(devops, 3);
            }
        });
        data_science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changecolor(data_science, 4);
            }
        });
        python.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changecolor(python, 5);
            }
        });



        //Загрузить начальный список
        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    User user = singleSnapshot.getValue(User.class);
                    //Проверка, чтобы не выводить в список себя
                    //!!! При обновлении могут добавляться уже находящиеся в списке объекты.
                    if (!currentUser.getUid().toString().equals(user.getUser_id()))
                        users.add(user);
                }
                searchUser = new ArrayList<User>(users);
                listViewUsers = (ListView) view.findViewById(R.id.list_of_users);
                adapter = new UserListAdapter(getActivity(),  users);
                listViewUsers.setAdapter(adapter);
                AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        // получаем пользователя, которого выбрали в списке
                        User selectedUser = (User)parent.getItemAtPosition(position);
                        UserProfileFragment userProfile = new UserProfileFragment();
                        Bundle bundle = new Bundle();
                        //Здесь заполняем bundle. решил не передавать класс, потому что пиздец как долго
                        bundle.putString("name", selectedUser.name);
                        bundle.putString("surname", selectedUser.surname);
                        bundle.putString("pass", selectedUser.pass);
                        bundle.putString("phone", selectedUser.phone);
                        bundle.putString("email", selectedUser.email);
                        bundle.putString("description", selectedUser.description);
                        bundle.putString("user_id", selectedUser.user_id);
                        bundle.putString("tag1", selectedUser.tag1);
                        bundle.putString("tag2", selectedUser.tag2);
                        bundle.putString("tag3", selectedUser.tag3);
                        bundle.putString("nickname", selectedUser.nickname);

                        //----------------------------------
                        userProfile.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, userProfile).
                                commit();
                    }
                };
                listViewUsers.setOnItemClickListener(itemListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 0){
                    searchUser = new ArrayList<User>(users);
                    adapter = new UserListAdapter(getActivity(),  searchUser);
                    listViewUsers.setAdapter(adapter);
                }
                else{
                    searchItem(s.toString(), before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    //Оптимальность под большим вопросом
    private void searchItem(String textToFind, int b, int s){
        if(b > s){
            searchUser = new ArrayList<User>(users);
        }

        ListIterator<User> listIter = searchUser.listIterator();
        String textToFindLower = textToFind.toLowerCase();
        String nameToLower;
        while (listIter.hasNext()) {
            User tmpUser = listIter.next();
            nameToLower = (tmpUser.getName() + " " + tmpUser.getSurname()).toLowerCase();
            if (!nameToLower.contains(textToFind)) {
                listIter.remove();
            }
        }
        adapter = new UserListAdapter(getActivity(), searchUser);
        listViewUsers.setAdapter(adapter);
    }

    private void changecolor(Button element, int i){
        if (array[i]==false) array[i]=true;
        else array[i]=false;
        if (array[i]==true){
            element.setBackgroundResource(R.drawable.btn_purple_less_radius);
            element.setTextColor(getResources().getColor(R.color.white));}
        else {element.setBackgroundResource(R.drawable.btn_purple_border);
            element.setTextColor(getResources().getColor(R.color.black));}

    }

}
