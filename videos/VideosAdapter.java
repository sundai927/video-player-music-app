package edu.ds4jb.videoplayer.videos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import edu.ds4jb.videoplayer.R;
//import edu.ds4jb.videoplayer.models.User;
import edu.ds4jb.videoplayer.models.User;
import edu.ds4jb.videoplayer.models.Video;
import edu.ds4jb.videoplayer.users.UserItemsViewHolder;

public class VideosAdapter extends RecyclerView.Adapter<VideoItemsViewHolder> {

    private final List<Video> videos = new ArrayList<>();
    public RequestManager requestManager;

    public VideosAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public VideoItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoItemsViewHolder(view, requestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemsViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }


    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void updateVideos(List<Video> videos) {
        this.videos.clear();
        this.videos.addAll(videos);
        notifyDataSetChanged();
    }
}
