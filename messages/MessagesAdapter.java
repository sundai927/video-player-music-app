package edu.ds4jb.videoplayer.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.models.Chat;
import edu.ds4jb.videoplayer.models.User;
import edu.ds4jb.videoplayer.users.UserItemsViewHolder;

public class MessagesAdapter extends RecyclerView.Adapter<MessageItemsViewHolder> {

    private final List<Chat> messages = new ArrayList<>();


    @NonNull
    @Override
    public MessageItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageItemsViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessages(List<Chat> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }
}
