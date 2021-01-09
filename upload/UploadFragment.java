package edu.ds4jb.videoplayer.upload;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.application.AppComponent;
import edu.ds4jb.videoplayer.home.HomeActivity;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {
    private static final String TAG = "UploadFragment";

    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int TAKE_VIDEO = 2;
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button open_files = view.findViewById(R.id.upload_from_files);
        Button record = view.findViewById(R.id.upload_from_camera);

        open_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, TAKE_VIDEO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PICKFILE_RESULT_CODE || requestCode == TAKE_VIDEO) && resultCode == RESULT_OK && data != null && data.getData() != null) {


            filePath = data.getData();
            //String path = filePath.toString();
            //Log.d(TAG, path);
            if (filePath != null) {
                final EditText editText = (EditText) requireView().findViewById(R.id.video_name);
                Button upload = requireView().findViewById(R.id.upload_named_video);
                upload.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        VideoUploader videoUploader = AppComponent.fromContext(requireContext()).videoUploader;
                        String videoName = editText.getText().toString();
                        videoUploader.uploadTestFile(videoName, filePath);
//                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                TextView success = requireView().findViewById(R.id.message);
//                                success.setText("File Uploaded!");
//                                Intent intent = new Intent(requireContext(), HomeActivity.class);
//                                startActivity(intent);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e(TAG, "Could not find file", e);
//                                TextView failure = requireView().findViewById(R.id.message);
//                                failure.setText("File couldn't be uploaded");
//                            }
//                        });
                    }
                });

            }
        }
    }
}
