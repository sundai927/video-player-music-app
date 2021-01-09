package edu.ds4jb.videoplayer.myvideos;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.player.PlayerActivity;

public class MyVideoItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "MyVideoItemsViewHolder";
    private final TextView videoLabel;
    private final ImageView deleteVidIcon;

    public Video boundVideo = new Video();

    public MyVideoItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        videoLabel = itemView.findViewById(R.id.MyVideo);
        deleteVidIcon = itemView.findViewById(R.id.delete_video);
        itemView.setOnClickListener(this);
    }

    public void bind(Video video) {
        boundVideo = video;
        videoLabel.setText(video.displayName);
        deleteVidIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentUserId =  AppComponent.fromContext(itemView.getContext()).loginRepository.getUserId();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference deleteVidRef = database.getReference("Videos/" + boundVideo.id);
                DatabaseReference deleteChatRef = database.getReference("Messages/" + boundVideo.id);
                deleteChatRef.removeValue();
                deleteVidRef.removeValue();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference deleteRef = storageReference.child("Users/" + currentUserId + "/Videos/" + boundVideo.fileName);
                deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), PlayerActivity.class);
        intent.putExtra("id", boundVideo.id);
        view.getContext().startActivity(intent);
    }

}
