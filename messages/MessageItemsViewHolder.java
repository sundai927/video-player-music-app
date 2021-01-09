package edu.ds4jb.videoplayer.messages;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.models.Chat;
import edu.ds4jb.videoplayer.models.User;
import edu.ds4jb.videoplayer.videos.VideoDisplayActivity;


public class MessageItemsViewHolder extends RecyclerView.ViewHolder  {

    private static final String TAG = "MessageItemsViewHolder";
    private final TextView messageLabel;
    private Chat boundChat = new Chat();
    public MessageItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        messageLabel = itemView.findViewById(R.id.user_message);
    }

    public void bind(Chat chat) {
        boundChat = chat;
        messageLabel.setText(chat.displayName + " " + chat.message);
    }


}
