package edu.ds4jb.videoplayer.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;

public class CreateAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText userId = (EditText) findViewById(R.id.new_user_ID);
        EditText displayName = (EditText) findViewById(R.id.new_display_name);
        Button createAccountFinal = findViewById(R.id.create_account_final);
        String currentUserId =  AppComponent.fromContext(this).loginRepository.getUserId();


        createAccountFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserId = userId.getText().toString();
                String newUserDisplay = displayName.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users/" + newUserId);
                myRef.child("displayName").setValue(newUserDisplay);
                myRef.child("id").setValue(newUserId);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference newUser = storageReference.child("Users/" + currentUserId + "/Videos/");
                Intent intent = new Intent(CreateAccountActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}
