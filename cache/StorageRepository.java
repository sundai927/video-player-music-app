package edu.ds4jb.videoplayer.cache;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class StorageRepository {
    private final Map<String, Uri> cache;
    public StorageRepository() {
         cache = new HashMap<>();
    }

    public void getDownloadUrl(String storagePath, OnSuccessListener<Uri> successListener, OnFailureListener failureListener) {
        if(cache.containsKey(storagePath)) {
            successListener.onSuccess(cache.get(storagePath));
        }
        else {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference reference = storageReference.child(storagePath);
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    cache.put(storagePath, uri);
                    successListener.onSuccess(uri);
                }
            }).addOnFailureListener(failureListener);


        }
    }
}
