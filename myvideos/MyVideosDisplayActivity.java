package edu.ds4jb.videoplayer.myvideos;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.videos.VideosAdapter;

public class MyVideosDisplayActivity extends AppCompatActivity {
    private static final String TAG = "MyVideosDisplayActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos);

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewMyVideos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        final MyVideosAdapter adapter = new MyVideosAdapter();
        recyclerView.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Videos");

        String currentUserId = AppComponent.fromContext(this).loginRepository.getUserId();

        myRef.orderByChild("uploader").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Method is called once with the initial value and again whenever data at this location is updated
                List<Video> videos = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {


                    videos.add(child.getValue(Video.class));


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
