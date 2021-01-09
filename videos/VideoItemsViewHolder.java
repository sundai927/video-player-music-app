package edu.ds4jb.videoplayer.videos;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.cache.StorageRepository;
import edu.ds4jb.videoplayer.models.User;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.player.PlayerActivity;

public class VideoItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView videoThumbnail;
    private final RequestManager requestManager;
    public Video boundVideo = new Video();

    public VideoItemsViewHolder(@NonNull View itemView, RequestManager requestManager) {
        super(itemView);
        this.requestManager = requestManager;
        videoThumbnail = itemView.findViewById(R.id.videoitem);
        itemView.setOnClickListener(this);
    }

    public void bind(Video video) {
        boundVideo = video;
        StorageRepository storageRepository = AppComponent.fromContext(this.itemView.getContext()).storageRepository;
        storageRepository.getDownloadUrl("Users/" + video.uploader + "/Videos/" + video.thumbnail, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                requestManager.load(uri).into(videoThumbnail);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Users/" + video.uploader + "/Videos/" + video.thumbnail);
//        Task<Uri> thumbnailPath = storageReference.getDownloadUrl();
//
//        thumbnailPath.addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                requestManager.load(uri).into(videoThumbnail);
//            }
//        });
//        videoLabel.setText(video.displayName);

    }

//    //private static String getFileName(TextView name) {
//        return (String) name.getText();
//    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), PlayerActivity.class);
        intent.putExtra("id", boundVideo.id);
        view.getContext().startActivity(intent);
    }
}
