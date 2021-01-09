package edu.ds4jb.videoplayer.feed;

import android.content.Intent;
import android.net.Uri;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.cache.StorageRepository;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.player.PlayerActivity;

public class VideoFeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final FrameLayout media_container;
    private final ImageView thumbnail;
    private final TextView displayName;
    private final ProgressBar progressBar;
    private final View parent;
    private final RequestManager requestManager;
    private final PlayerView playerView;
    private  Video currentVideo;
    private final AnalyticsListener analyticsListener = new AnalyticsListener() {
        @Override
        public void onRenderedFirstFrame(EventTime eventTime, @Nullable Surface surface) {
            thumbnail.setVisibility(View.INVISIBLE);
        }
    };


    public VideoFeedViewHolder(@NonNull View itemView, RequestManager requestManager) {
        super(itemView);

        this.requestManager = requestManager;
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        displayName = itemView.findViewById(R.id.display_name);
        progressBar = itemView.findViewById(R.id.progressBar);
        playerView = itemView.findViewById(R.id.exoplayer_view);
        parent.setOnClickListener(this);
    }


    public void onBind(Video video) {
        this.currentVideo = video;
        parent.setTag(this);
        displayName.setText(video.uploader);
        StorageRepository storageRepository = AppComponent.fromContext(this.itemView.getContext()).storageRepository;
        storageRepository.getDownloadUrl("Users/" + video.uploader + "/Videos/" + video.thumbnail, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                requestManager.load(uri).into(thumbnail);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Users/" + video.uploader + "/Videos/" + video.thumbnail);
//        Task<Uri> thumbnailPath = storageReference.getDownloadUrl();

//        thumbnailPath.addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                requestManager.load(uri).into(thumbnail);
//            }
//        });

    }
    public void playVideo(SimpleExoPlayer player) {
        playerView.setPlayer(player);
        player.addAnalyticsListener(analyticsListener);
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        StorageRepository storageRepository = AppComponent.fromContext(this.itemView.getContext()).storageRepository;
        storageRepository.getDownloadUrl("Users/" + currentVideo.uploader + "/Videos/" + currentVideo.fileName, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                MediaItem mediaItem = MediaItem.fromUri(uri);
                player.setMediaItem(mediaItem);
                player.prepare();
                player.play();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Users/" + currentVideo.uploader + "/Videos/" + currentVideo.fileName);
//        Task<Uri> task = storageReference.getDownloadUrl();
//        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//                MediaItem mediaItem = MediaItem.fromUri(uri);
//                player.setMediaItem(mediaItem);
//                player.prepare();
//                player.play();
//            }
//        });

    }
    public void stopVideo() {
        thumbnail.setVisibility(View.VISIBLE);
        playerView.getPlayer().stop();
        ((SimpleExoPlayer) playerView.getPlayer()).removeAnalyticsListener(analyticsListener);
        playerView.setPlayer(null);
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), PlayerActivity.class);
        intent.putExtra("id", currentVideo.id);
        view.getContext().startActivity(intent);
    }
}
