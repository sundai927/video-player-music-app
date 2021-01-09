package edu.ds4jb.videoplayer.feed;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import edu.ds4jb.videoplayer.R;
import edu.ds4jb.videoplayer.models.Chat;
import edu.ds4jb.videoplayer.models.Video;

public class VideoFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Video> videos = new ArrayList<>();
    public RequestManager requestManager;

    public VideoFeedAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoFeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_list_item, parent, false), requestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VideoFeedViewHolder) holder).onBind(videos.get(position));
    }

    public void updateFeed(List<Video> videos) {
        this.videos.clear();
        this.videos.addAll(videos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
