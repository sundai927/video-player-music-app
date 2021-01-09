package edu.ds4jb.videoplayer.videos;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.models.Video;

public class VideoDisplayActivity extends AppCompatActivity {
    private static final String TAG = "VideoDisplayActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewVideo);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        final VideosAdapter adapter = new VideosAdapter(Glide.with(this));
        recyclerView.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Videos");

        myRef.orderByChild("uploader").equalTo(getIntent().getStringExtra("uploader")).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Method is called once with the initial value and again whenever data at this location is updated
                List<Video> videos = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    videos.add(child.getValue(Video.class));
//                    if((child.getValue(Video.class)).uploader.equals(getIntent().getStringExtra("uploader"))) {
//                        Log.d(TAG, "REACHED");
//                        videos.add(child.getValue(Video.class));
//                    }


                }


                adapter.updateVideos(videos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
