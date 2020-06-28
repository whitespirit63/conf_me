package com.hfad.conf_me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.conf_me.models.ChatMessage;
import com.hfad.conf_me.models.DialogItem;
import com.hfad.conf_me.models.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.hfad.conf_me.R.layout.dialog_list_item;
import static com.hfad.conf_me.R.layout.fragment_dialogs;

public class DialogsFragment extends Fragment {

    private List<DialogItem> dialogs = new ArrayList();

    //private List<ChatMessage> searchDialogs = new ArrayList();
    private ListView listViewDialogs;
    ChatMessageListAdapter adapter;
    String user_name;
    String user_id;
    String last_message;
    String currentUid;
    String id;
    DataSnapshot singlePostData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(fragment_dialogs, container, false);
        View root = inflater.inflate(dialog_list_item, container, false);

        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usersDataBase = FirebaseDatabase.getInstance().getReference("Users");
        currentUid = userId.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Dialogs").child(currentUid);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    long maxCount = singleSnapshot.getChildrenCount();
                    long count = 0;
                    for(DataSnapshot snapshot: singleSnapshot.getChildren()) {
                        if(count == maxCount - 1) {
                            ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                            dialogs.add(new DialogItem(
                                    "Name", "Surname", singleSnapshot.getKey(), chatMessage.getTextMessage()));
                        }
                        count++;
                    }
                }

                //searchDialogs = new ArrayList<ChatMessage>(dialogs);
                listViewDialogs = (ListView) rootView.findViewById(R.id.list_of_dialogs);
                adapter = new ChatMessageListAdapter(getActivity(), dialogs);
                listViewDialogs.setAdapter(adapter);
                AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        // получаем пользователя, которого выбрали в списке
                        DialogItem selectedDialog = (DialogItem) parent.getItemAtPosition(position);
                        DialogFragment dialogFragment = new DialogFragment();
                        Bundle bundle = new Bundle();
                        //Здесь заполняем bundle. решил не передавать класс, потому что пиздец как долго

                        bundle.putString("user_id", selectedDialog.userID);
                        //----------------------------------
                        dialogFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, dialogFragment).
                                commit();
                    }
                };
                listViewDialogs.setOnItemClickListener(itemListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootView;
    }

    private void displayAllDialogs(View rootView, View root) {

    }


}





