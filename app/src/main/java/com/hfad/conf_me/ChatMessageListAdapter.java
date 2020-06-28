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

import com.hfad.conf_me.models.ChatMessage;
import com.hfad.conf_me.models.DialogItem;
import com.hfad.conf_me.models.User;

import java.util.List;

public class ChatMessageListAdapter extends ArrayAdapter<DialogItem> {
    private Context context;
    private LayoutInflater inflater;
    private List<DialogItem> chatMessages;

    public ChatMessageListAdapter(Context context, List<DialogItem> chatMessages){
        super(context, R.layout.dialog_list_item, chatMessages);
        this.chatMessages = chatMessages;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(R.layout.dialog_list_item, parent, false);

        TextView user_name = (TextView) view.findViewById(R.id.user_name);
        TextView last_message = (TextView) view.findViewById(R.id.last_message);
        DialogItem chatMessage = chatMessages.get(position);

        user_name.setText(chatMessage.getUserName() + " " + chatMessage.getUserSurname());
        last_message.setText(chatMessage.getLastMessage());

        return view;
    }
}
