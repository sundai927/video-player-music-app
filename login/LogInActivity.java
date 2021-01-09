package edu.ds4jb.videoplayer.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.home.HomeActivity;
import edu.ds4jb.videoplayer.models.User;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LogInActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AppComponent.fromContext(LogInActivity.this).loginRepository.getIsLoggedIn()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        Button login = findViewById(R.id.login_button);
        Button createAccount = findViewById(R.id.create_account);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                final EditText editText = (EditText) findViewById(R.id.type_login);
                AppComponent.fromContext(LogInActivity.this).loginRepository.login(editText.getText().toString());
                final String currentUserId = AppComponent.fromContext(LogInActivity.this).loginRepository.getUserId();

                // Check if User ID exists
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users/" + currentUserId);
                myRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Method is called once with the initial value and again whenever data at this location is updated


                        //Log.d(TAG, child.getValue(User.class).id);
                        if (snapshot.exists()) {
                            startActivity(intent);
                            finish();
                        } else {
                            editText.getText().clear();
                            editText.setHint("Invalid User ID");
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
        // Create Account button
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
