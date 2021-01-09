package edu.ds4jb.videoplayer.record;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.upload.VideoUploader;

public class RecordActivity extends AppCompatActivity {
    private static final String TAG = "RecordActivity";
    private static int TAKE_VIDEO = 1;
    private Uri filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        record();
    }

    private void record() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, TAKE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            if (filePath != null) {
                final EditText editText = (EditText) findViewById(R.id.video_name);
                Button upload = findViewById(R.id.upload_named_video);
                upload.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        VideoUploader videoUploader = AppComponent.fromContext(RecordActivity.this).videoUploader;
                        String videoName = editText.getText().toString();
                        videoUploader.uploadTestFile(videoName, filePath);
//                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                TextView success = findViewById(R.id.message);
//                                success.setText("File Uploaded!");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e(TAG, "Could not find file", e);
//                                TextView failure = findViewById(R.id.message);
//                                failure.setText("File couldn't be uploaded");
//                            }
//                        });

                    }
                });

            }
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.N)

//    public void uploadTestFile(String name) {
//
//        // Upload local video to Firebase Storage
//
//        StorageReference storageReference;
//        storageReference = FirebaseStorage.getInstance().getReference();
//        String currentUserId =  AppComponent.fromContext(this).loginRepository.getUserId();
//        Log.d(TAG, currentUserId);
//        // File downloads = getExternalFilesDir(null);
//        // File newFile = new File(downloads, "rob.mp4");
//        // Uri file = Uri.fromFile(newFile);
//
//        // Upload video to storage corresponding to current user
//        StorageReference ref = storageReference.child("Users/" + currentUserId + "/Videos/" + name + ".mp4");
//        ref.putFile(filePath)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        TextView success = findViewById(R.id.message);
//                        success.setText("File Uploaded!");
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //Handle upload failure
//                        Log.e(TAG, "Could not find file", e);
//                        TextView failure = findViewById(R.id.message);
//                        failure.setText("File couldn't be uploaded");
//
//                    }
//                });
//// Generate random video id
//        int leftLimit = 48; // numeral '0'
//        int rightLimit = 122; // letter 'z'
//        int targetStringLength = 10;
//        Random random = new Random();
//
//        String generatedString = random.ints(leftLimit, rightLimit + 1)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                .limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                .toString();
//// Add uploaded video information to database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Videos/" + generatedString);
//        myRef.child("displayName").setValue(name);
//        myRef.child("fileName").setValue(name + ".mp4");
//        myRef.child("id").setValue(generatedString);
//        myRef.child("uploader").setValue(currentUserId);
//
//
//    }
}
