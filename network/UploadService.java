package edu.ds4jb.videoplayer.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UploadService {
    @POST("/video-upload")
    Call<ResponseBody> uploadVideo(@Body RequestBody body, @Query("displayName") String displayName, @Query("uploader") String uploader);
}
