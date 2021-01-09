package edu.ds4jb.videoplayer.users;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import edu.ds4jb.videoplayer.R;

import edu.ds4jb.videoplayer.models.User;
import edu.ds4jb.videoplayer.videos.VideoDisplayActivity;


public class UserItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "UserItemsViewHolder";
    private final TextView nameLabel;
    private User boundUser = new User();
    public UserItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        nameLabel = itemView.findViewById(R.id.Name);
        itemView.setOnClickListener(this);
    }

    public void bind(User user) {
        boundUser = user;
        nameLabel.setText(user.displayName);
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), VideoDisplayActivity.class);
        intent.putExtra("uploader", boundUser.id);
        view.getContext().startActivity(intent);

    }
}
