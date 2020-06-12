package com.hfad.conf_me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.conf_me.models.ChatMessage;

import org.jetbrains.annotations.NotNull;

import static com.hfad.conf_me.R.layout.fragment_dialog;
import static com.hfad.conf_me.R.layout.list_item;

public class DialogFragment extends Fragment {

    private FirebaseListAdapter<ChatMessage> adapter;
    private FloatingActionButton sendBtn;
    String user_name;
    String user_id;
    String currentUid;
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(fragment_dialog, container, false);
        View root = inflater.inflate(list_item, container, false);
        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUid = userId.getUid();

        Bundle bundle = getArguments();
        user_name = bundle.getString("name");
        user_id = bundle.getString("user_id");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Dialogs");
        DatabaseReference myRef = database.child(currentUid);
        sendBtn = rootView.findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText textField = rootView.findViewById(R.id.message_field);
                if (textField.getText().toString() == "")
                    return;

                database.child(currentUid).child(user_id).push().setValue(
                        new ChatMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                textField.getText().toString()
                        )
                );
                database.child(user_id).child(currentUid).push().setValue(
                        new ChatMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                textField.getText().toString()
                        )
                );
                textField.setText("");
            }
        });
        displayAllMessages(rootView, root);
        return rootView;
    }
    private void displayAllMessages(View rootView,View root){
        FirebaseUser userId = FirebaseAuth.getInstance().getCurrentUser() ;
        currentUid = userId.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Dialogs");
        DatabaseReference myRef = database.child("Users").child(currentUid).child("Dialogs");


        ListView listOfMessages = (ListView) rootView.findViewById(R.id.list_of_messages);
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()

                .setQuery(database.child(currentUid).child(user_id), ChatMessage.class)
                .setLayout(R.layout.list_item)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NotNull View v, ChatMessage model, int position) {
                RelativeLayout rootElement = (RelativeLayout) v.findViewById(R.id.root_element);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextView mess_time, mess_text, mess_userID;
                //mess_time= v.findViewById(R.id.message_time);
                mess_text= v.findViewById(R.id.message_text);

                mess_text.setText(model.getTextMessage());
                if(model.getUserID()!=null && model.getUserID().equals(currentUid)){
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    rootElement.setLayoutParams(params);

                    rootElement.setBackgroundResource(R.drawable.message_my_user);
                }

                //mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));

            }
        };
        listOfMessages.setAdapter(adapter);
    }

}
