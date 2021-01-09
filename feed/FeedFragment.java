package edu.ds4jb.videoplayer.feed;

import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.videos.VideosAdapter;

public class FeedFragment extends Fragment {
    private static final String TAG = "FeedFragment";
    SimpleExoPlayer exoPlayer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exoPlayer = new SimpleExoPlayer.Builder(this.requireContext()).build();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewFeed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        final VideoFeedAdapter adapter = new VideoFeedAdapter(Glide.with(this));
        recyclerView.setAdapter(adapter);

        Rect rect = new Rect();
        Point point = new Point();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            VideoFeedViewHolder currentVideo;

            int currentVideoPosition = -1;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int desiredPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(desiredPosition == RecyclerView.NO_POSITION) {
                    if(currentVideoPosition >= linearLayoutManager.findFirstVisibleItemPosition()
                            && currentVideoPosition <= linearLayoutManager.findLastVisibleItemPosition()) {
                        desiredPosition = currentVideoPosition;
                    }
                    else {
                        desiredPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    }
                }
                currentVideoPosition = desiredPosition;
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View focusedVideo = recyclerView.getChildAt(i);
                    if (desiredPosition == recyclerView.getChildAdapterPosition(focusedVideo)) {


                        VideoFeedViewHolder viewHolder = (VideoFeedViewHolder) recyclerView.getChildViewHolder(focusedVideo);
                        if (viewHolder != currentVideo) {

                            if (currentVideo != null) {
                                currentVideo.stopVideo();
                            }
                            currentVideo = viewHolder;
                            currentVideo.playVideo(exoPlayer);

                        }
                        break;
                    }
                }
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Videos");

        myRef.orderByChild("timeStamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Video> videos = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    videos.add(child.getValue(Video.class));
//                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//                    Video playVideo = child.getValue(Video.class);
//                    String uploader = playVideo.uploader;
//                    String videoFile = playVideo.fileName;
//                    StorageReference reference = storageReference.child("Users/" + uploader + "/Videos/" + videoFile);
//                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            //videoView.setVideoURI(uri);
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.e(TAG, "Could not download file");
//                        }
//                    });
                }
                adapter.updateFeed(videos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        exoPlayer.setPlayWhenReady(true);
    }
}


