package edu.ds4jb.videoplayer.application;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import edu.ds4jb.videoplayer.cache.StorageRepository;
import edu.ds4jb.videoplayer.login.LoginRepository;
import edu.ds4jb.videoplayer.network.UploadService;
import edu.ds4jb.videoplayer.upload.VideoUploader;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class AppComponent extends Application {
    public static final String TAG = "AppComponent";
    public LoginRepository loginRepository;
    public VideoUploader videoUploader;
    public UploadService uploadService;
    public StorageRepository storageRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        loginRepository = new LoginRepository(this);
        storageRepository = new StorageRepository();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        uploadService = new Retrofit.Builder()
                .baseUrl("http://192.168.0.152/")
                .client(okHttpClient)
                .build()
                .create(UploadService.class);
        videoUploader = new VideoUploader(loginRepository, uploadService, this.getContentResolver());
        //Log.d(TAG, "Reached");
    }
    public static AppComponent fromContext(Context context) {
       return (AppComponent) context.getApplicationContext();
    }

}
