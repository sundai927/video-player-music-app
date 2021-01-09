package edu.ds4jb.videoplayer.player;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;

import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
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
import java.util.Random;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.cache.StorageRepository;
import edu.ds4jb.videoplayer.messages.MessagesAdapter;
import edu.ds4jb.videoplayer.models.Chat;
import edu.ds4jb.videoplayer.models.User;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.users.UsersAdapter;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";
    private SimpleExoPlayer exoPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        final PlayerView playerView = findViewById(R.id.playerview);
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);


        playerView.setPlayer(exoPlayer);


        String videoId = getIntent().getStringExtra("id");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Videos/" + videoId);

        StorageRepository storageRepository = AppComponent.fromContext(this).storageRepository;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Video playVideo = snapshot.getValue(Video.class);
                String uploader = playVideo.uploader;
                String videoFile = playVideo.fileName;


                storageRepository.getDownloadUrl("Users/" + uploader + "/Videos/" + videoFile, new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        MediaItem mediaItem = MediaItem.fromUri(uri);
                        exoPlayer.setMediaItem(mediaItem);
                        exoPlayer.prepare();
                        exoPlayer.play();
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Could not download file");
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Set up Video Player Screen

        //String path = "android.resource://" + getPackageName() + "/" + R.raw.rob;
       // Uri uri = Uri.parse(path);


        // Set up Media Controller
//        MediaController mediaController = new MediaController(this);
//        exoPlayer.setMediaController(mediaController);
//        mediaController.setAnchorView(videoView);

        EditText chatBox = findViewById(R.id.chat_box);
        ImageView sendChat = findViewById(R.id.send_chat);
        RecyclerView recyclerView = findViewById(R.id.chat_screen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        final MessagesAdapter adapter = new MessagesAdapter();
        recyclerView.setAdapter(adapter);



        DatabaseReference chatRef = database.getReference("Messages/" + videoId);
        chatRef.orderByChild("timeStamp").limitToLast(6).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Chat> messages = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    messages.add(child.getValue(Chat.class));
                }
                adapter.updateMessages(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendChat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(!chatBox.getText().toString().equals("")) {
                    String message = chatBox.getText().toString();
                    // Generate random message id
                    int leftLimit = 48; // numeral '0'
                    int rightLimit = 122; // letter 'z'
                    int targetStringLength = 10;
                    Random random = new Random();

                    String generatedString = random.ints(leftLimit, rightLimit + 1)
                            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                            .limit(targetStringLength)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();

                    String currentUserId =  AppComponent.fromContext(PlayerActivity.this).loginRepository.getUserId();
                    DatabaseReference ref =database.getReference("Messages/" + videoId + "/" + generatedString);
                    DatabaseReference userRef = database.getReference("Users/" + currentUserId);

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String curDisplayName = snapshot.child("displayName").getValue(String.class);
                            Chat chat = new Chat(curDisplayName, message, System.currentTimeMillis());
                            ref.setValue(chat);
                            //ref.child("displayName").setValue(curDisplayName);
                            //ref.child("message").setValue(message);
                            chatBox.getText().clear();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                   // Log.d(TAG, curDisplayName[0]);

                }

            }
        });














    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        exoPlayer.setPlayWhenReady(true);
    }
}
