package edu.ds4jb.videoplayer.upload;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.util.Log;
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

import java.io.File;
import java.io.IOException;
import java.util.Random;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.login.LoginRepository;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.network.UploadService;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VideoUploader {
    private static final String TAG = "VideoUploader";
    private final LoginRepository loginRepository;
    private final UploadService uploadService;
    private final ContentResolver contentResolver;

    public VideoUploader(LoginRepository loginRepository, UploadService uploadService, ContentResolver contentResolver) {
        this.loginRepository = loginRepository;
        this.uploadService = uploadService;
        this.contentResolver = contentResolver;
    }

    private long getFileSize(Uri uri) {
        Cursor returnCursor =
                contentResolver.query(uri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        return returnCursor.getLong(sizeIndex);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void uploadTestFile(String name, Uri filePath) {

        // Generate random video id
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        // Upload local video to Firebase Storage

//        StorageReference storageReference;
//        storageReference = FirebaseStorage.getInstance().getReference();
        String currentUserId = loginRepository.getUserId();
        //Log.d(TAG, currentUserId);
        // File downloads = getExternalFilesDir(null);
        // File newFile = new File(downloads, "rob.mp4");
        // Uri file = Uri.fromFile(newFile);

        // Upload video to storage corresponding to current user
//        StorageReference ref = storageReference.child("Users/" + currentUserId + "/Videos/" + generatedString + ".mp4");

//        UploadTask uploadTask = ref.putFile(filePath);
        File uploadFile = new File(filePath.getPath());
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.get(contentResolver.getType(filePath));
            }

            @Override
            public long contentLength() throws IOException {
                return getFileSize(filePath);
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                try (Source source = Okio.source(contentResolver.openInputStream(filePath))) {
                    sink.writeAll(source);
                }
            }
        };
        Call<ResponseBody> networkCall = uploadService.uploadVideo(requestBody, name, currentUserId);

        networkCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Upload Response");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Upload Failure", t);
            }
        });




// Add uploaded video information to database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Videos/" + generatedString);
//        Video uploadVideo = new Video(generatedString + ".mp4", name, currentUserId, generatedString,System.currentTimeMillis(), generatedString + ".jpg");
//        myRef.setValue(uploadVideo);
//
//        myRef.child("displayName").setValue(name);
//        myRef.child("fileName").setValue(name + ".mp4");
//        myRef.child("id").setValue(generatedString);
//        myRef.child("uploader").setValue(currentUserId);
    }
}
