package edu.ds4jb.videoplayer.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.feed.FeedFragment;
import edu.ds4jb.videoplayer.myvideos.MyVideosFragment;
import edu.ds4jb.videoplayer.upload.UploadFragment;
import edu.ds4jb.videoplayer.users.UsersFragment;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Set the display name of currently logged in user on the top of the screen
       // final TextView currentUser = findViewById(R.id.current_user);
//        String currentUserId =  AppComponent.fromContext(this).loginRepository.getUserId();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Users/" + currentUserId);
//        myRef.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Method is called once with the initial value and again whenever data at this location is updated
//
//                String value = snapshot.getValue(User.class).displayName;
//                //currentUser.setText(value);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        BottomNavigationViewEx bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.enableAnimation(false);

        bottomNavigationView.enableShiftingMode(false);

        bottomNavigationView.enableItemShiftingMode(false);




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_home:
                        item.setCheckable(true);
                        FeedFragment fragment_feed = new FeedFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment_feed).commit();
                        break;
                    case R.id.action_upload:

                        UploadFragment fragment_upload = new UploadFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment_upload).commit();
//                        Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
//                        startActivity(intent);
                        break;

                    case R.id.action_my_profile:
                        MyVideosFragment fragment_profile = new MyVideosFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment_profile).commit();
//                        Intent intent_myprofile = new Intent(HomeActivity.this, MyVideosDisplayActivity.class);
//                        startActivity(intent_myprofile);
                        break;

//                    case R.id.action_record:
//                        Intent intent_record = new Intent(HomeActivity.this, RecordActivity.class);
//                        startActivity(intent_record);
//                        break;
                    case R.id.action_users:
                        UsersFragment fragment_users = new UsersFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment_users).commit();
//                        Intent intent_users = new Intent(HomeActivity.this, UserDisplayActivity.class);
//                        startActivity(intent_users);
                        break;
                }


                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        // Player, Upload, and Users TextViews
        //TextView player = findViewById(R.id.player_button);
//        TextView upload = findViewById(R.id.upload_button);
//        TextView users = findViewById(R.id.users_button);
//        TextView record = findViewById(R.id.record_button);
        //TextView logout = findViewById(R.id.logout_button);
//        TextView myVideos = findViewById(R.id.my_videos_button);
       // player.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, PlayerActivity.class);
//                startActivity(intent);
//            }
//        });

//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, UploadActivity.class);
//                startActivity(intent);
//            }
//        });
//        users.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, UserDisplayActivity.class);
//                startActivity(intent);
//            }
//        });
//        record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, RecordActivity.class);
//                startActivity(intent);
//            }
//        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppComponent.fromContext(HomeActivity.this).loginRepository.logout();
//                Intent intent = new Intent(HomeActivity.this, LogInActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//        myVideos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, MyVideosDisplayActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
